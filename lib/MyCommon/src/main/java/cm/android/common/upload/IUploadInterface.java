package cm.android.common.upload;

public interface IUploadInterface {
    public static final int OPERATE_START = 0x01;
    public static final int OPERATE_STOP = OPERATE_START + 1;
    public static final int OPERATE_DELETE = OPERATE_START + 2;

    void start();

    void stop();

    void delete();

    boolean isStopped();

    boolean isDeleted();

    boolean isWaiting();
}
