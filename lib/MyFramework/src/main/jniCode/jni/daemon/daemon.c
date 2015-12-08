#include <sys/types.h>
#include <sys/stat.h>
#include <sys/wait.h>
#include <sys/system_properties.h>
#include <stdlib.h>
#include <stdio.h>
#include <signal.h>
#include <string.h>
#include <fcntl.h>
#include <unistd.h>
#include <errno.h>

#include "common.h"

#define LOG_TAG         "Daemon"
#define	MAXFILE         3
#define SLEEP_INTERVAL  2 * 60

volatile int sig_running = 1;

/* signal term handler */
static void sigterm_handler(int signo)
{
    LOGI("handle signal: %d ", signo);
    sig_running = 0;
}

/* start daemon service */
static void start_service(char *package_name, char *service_name)
{
    /* get the sdk version */
    int version = get_version();

    pid_t pid;

    if ((pid = fork()) < 0)
    {
        exit(EXIT_SUCCESS);
    }
    else if (pid == 0)
    {
        if (package_name == NULL || service_name == NULL)
        {
            LOGI("package name or service name is null");
            return;
        }

        char *p_name = str_stitching(package_name, "/");
        char *s_name = str_stitching(p_name, service_name);
        LOGI("service: %s", s_name);

        if (version >= 17 || version == 0)
        {
            int ret = execlp("am", "am", "startservice",
                             "--user", "0", "-n", s_name, (char *) NULL);
            LOGI("result %d", ret);
        }
        else
        {
            execlp("am", "am", "startservice", "-n", s_name, (char *) NULL);
        }

        LOGI("exit start-service child process");
        exit(EXIT_SUCCESS);
    }
    else
    {
        waitpid(pid, NULL, 0);
    }
}

void cleanProcess(char *processName) {
    /* find pid by name and kill them */
    int pid_list[100];
    int i = 0;
    int total_num = find_pid_by_name(processName, pid_list);
    LOGI("cleanProcess total num %d", total_num);
    for (i = 0; i < total_num; i++) {
        int retval = 0;
        int daemon_pid = pid_list[i];
        if (daemon_pid > 1 && daemon_pid != getpid()) {
            retval = kill(daemon_pid, SIGKILL);
            if (!retval) {
                LOGI("kill daemon process success: %d", daemon_pid);
            } else {
                LOGI("kill daemon process %d fail: %s", daemon_pid, strerror(errno));
                exit(EXIT_SUCCESS);
            }
        }
    }
}

int main(int argc, char *argv[])
{
    int i;
    pid_t pid;
    char *package_name = NULL;
    char *service_name = NULL;
    char *daemon_file_dir = NULL;
    int interval = SLEEP_INTERVAL;
    int mode = 0;  // 0：创建子进程  1：杀死子进程

    if (argc < 9) {
        LOGI("usage: %s -p package-name -s "
                     "daemon-service-name -t interval-time", argv[0]);
        return;
    }

    for (i = 0; i < argc; i++) {
        LOGI("argv %d = %s", i, argv[i]);
        //包名
        if (!strcmp("-p", argv[i])) {
            package_name = argv[i + 1];
            LOGI("package name: %s", package_name);
        }

        //服务名
        if (!strcmp("-s", argv[i])) {
            service_name = argv[i + 1];
            LOGI("service name: %s", service_name);
        }

        //轮询间隔
        if (!strcmp("-t", argv[i])) {
            interval = atoi(argv[i + 1]);
            LOGI("interval: %d", interval);
        }

        //创建子进程或者杀死子进程
        if (!strcmp("-m", argv[i])) {
            mode = atoi(argv[i + 1]);
            LOGI("mode: %d", mode);
        }
    }
    if (mode == 1) {
        // 杀死所有子进程
        cleanProcess(argv[0]);
        sig_running = 0;
    }

    /* package name and service name should not be null */
    if (package_name == NULL || service_name == NULL)
    {
        LOGI("package name or service name is null");
        return;
    }

    if ((pid = fork()) < 0)
    {
        exit(EXIT_SUCCESS);
    }
    else if (pid == 0)
    {
        /* add signal */
        signal(SIGKILL, sigterm_handler);

        /* become session leader */
        setsid();
        /* change work directory */
        chdir("/");

        for (i = 0; i < MAXFILE; i ++)
        {
            close(i);
        }

        /* find pid by name and kill them */
//		int pid_list[100];
//		int total_num = find_pid_by_name(argv[0], pid_list);
//		LOGD(LOG_TAG, "total num %d", total_num);
//		for (i = 0; i < total_num; i ++)
//		{
//			int retval = 0;
//			int daemon_pid = pid_list[i];
//			if (daemon_pid > 1 && daemon_pid != getpid())
//			{
//				retval = kill(daemon_pid, SIGTERM);
//				if (!retval)
//				{
//					LOGD(LOG_TAG, "kill daemon process success: %d", daemon_pid);
//				}
//				else
//				{
//					LOGD(LOG_TAG, "kill daemon process %d fail: %s", daemon_pid, strerror(errno));
//					exit(EXIT_SUCCESS);
//				}
//			}
//		}

        cleanProcess(argv[0]);
        LOGI("child process fork ok, daemon start: %d", getpid());

        while(sig_running)
        {
            interval = interval < SLEEP_INTERVAL ? SLEEP_INTERVAL : interval;
            select_sleep(interval, 0);

            LOGI("check the service once, interval: %d", interval);

            /* start service */
            start_service(package_name, service_name);
        }

        exit(EXIT_SUCCESS);
    }
    else
    {
        /* parent process */
        exit(EXIT_SUCCESS);
    }
}