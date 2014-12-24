package cm.android.common.upload;

public interface IUploader<T> {

    /**
     * 上传核心方法
     */
    boolean upload(T task, IUploadListener<T> iUploadListener)
            throws UploadException;

    void reset();
}
