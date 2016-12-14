package cm.android.framework.client.core;

import cm.android.framework.server.ServerProvider;

public class Config {
    private String serverProcess;
    private String authorities;

    private Config(Config.Builder builder) {
        authorities = builder.authorities;
        serverProcess = builder.serverProcess;
    }

    public String getServerProcess() {
        return serverProcess;
    }

    public String getAuthorities() {
        return authorities;
    }

    public static final class Builder {
        private String serverProcess;
        private String authorities;

        public Builder() {
            serverProcess = null;
            authorities = ServerProvider.AUTHORITIES;
        }

        public Config.Builder serverProcess(String serverProcess) {
            this.serverProcess = serverProcess;
            return this;
        }

        public Config.Builder authorities(String authorities) {
            this.authorities = authorities;
            return this;
        }

        public Config build() {
            return new Config(this);
        }
    }
}
