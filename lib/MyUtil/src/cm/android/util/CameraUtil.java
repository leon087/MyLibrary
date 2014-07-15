package cm.android.util;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * 调用系统应用进行拍照、摄像、录音、选取图片等操作，某些操作需要获取权限
 * <p/>
 * <uses-permission android:name="android.permission.GET_TASKS" />
 * <uses-permission android:name="android.permission.RECORD_AUDIO" />
 * <uses-permission android:name="android.permission.CAMERA" />
 */
public class CameraUtil {
    /**
     * 调用系统照相机拍摄照片
     */
    public static final int REQUEST_CODE_TAKE_PHOTO = 0xF1;
    /**
     * 调用系统照相机摄像
     */
    public static final int REQUEST_CODE_TAKE_VIDEO = REQUEST_CODE_TAKE_PHOTO + 1;
    /**
     * 调用系统应用录音
     */
    public static final int RESULT_CAPTURE_RECORDER_SOUND = REQUEST_CODE_TAKE_PHOTO + 2;
    /**
     * 从相册中选择一张图片
     */
    public static final int REQUEST_CODE_PICK_PHOTO = REQUEST_CODE_TAKE_PHOTO + 3;
    /**
     * 从视频库中选择视频
     */
    public static final int REQUEST_CODE_PICK_VIDEO = REQUEST_CODE_TAKE_PHOTO + 4;
    /**
     * 裁剪图片
     */
    public static final int REQUEST_CODE_CROP = REQUEST_CODE_TAKE_PHOTO + 5;

    private CameraUtil() {
    }

    private static Intent takeVideoIntent() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
        // 限制10s的录像
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 10);
        return intent;
    }

    private static Intent pickPhotoIntent() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        return intent;
    }

    private static Intent pickVideoIntent() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("video/*"); // String VIDEO_UNSPECIFIED = "video/*";
        // Intent wrapperIntent = Intent.createChooser(innerIntent, null);
        return intent;
    }

    private static Intent takeSoundRecorderIntent() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("audio/amr");
        return intent;
    }

    private static Intent cropImageIntent(Uri uri, int width, int height) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");

        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", width);
        intent.putExtra("outputY", height);
        // true:返回数据为bitmap，false:返回uri
        intent.putExtra("return-data", true);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        // 取消人脸识别
        intent.putExtra("noFaceDetection", true);
        return intent;
    }

    private static Intent cropImageIntent(Uri uri, Uri outputUri, int width,
                                          int height) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
        // intent.putExtra("data", data);
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");

        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", width);
        intent.putExtra("outputY", height);
        // true:返回数据为bitmap，false:返回uri
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        // 取消人脸识别
        intent.putExtra("noFaceDetection", true);
        return intent;
    }

    /**
     * 拍照获取图片
     */
    public static Uri takePhoto(Activity activity) {
        // 执行拍照前，应该先判断SD卡是否存在
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// "android.media.action.IMAGE_CAPTURE"
        /***
         * 需要说明一下，以下操作使用照相机拍照，拍照后的图片会存放在相册中的 这里使用的这种方式有一个好处就是获取的图片是拍照后的原图
         * 如果不实用ContentValues存放照片路径的话，拍照后获取的图片为缩略图不清晰
         */
        ContentValues values = new ContentValues();
        Uri photoUri = activity.getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoUri);
        activity.startActivityForResult(intent, REQUEST_CODE_TAKE_PHOTO);
        return photoUri;
    }

    /**
     * 拍摄视频
     */
    public static void takeVideo(Activity activity) {
        Intent intent = takeVideoIntent();
        activity.startActivityForResult(intent, REQUEST_CODE_TAKE_VIDEO);
    }

    /**
     * 录音功能
     */
    public static void takeSoundRecorder(Activity activity) {
        Intent intent = takeSoundRecorderIntent();
        activity.startActivityForResult(intent, RESULT_CAPTURE_RECORDER_SOUND);
    }

    public static void pickPhoto(Activity activity) {
        Intent intent = pickPhotoIntent();
        activity.startActivityForResult(intent, REQUEST_CODE_PICK_PHOTO);
    }

    /**
     * 选取视频
     */
    public static void pickVideo(Activity activity) {
        Intent intent = pickVideoIntent();
        activity.startActivityForResult(intent, REQUEST_CODE_PICK_VIDEO);
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////

    public static Uri takePhoto(android.support.v4.app.Fragment fragment) {
        // 执行拍照前，应该先判断SD卡是否存在
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// "android.media.action.IMAGE_CAPTURE"
        /***
         * 需要说明一下，以下操作使用照相机拍照，拍照后的图片会存放在相册中的 这里使用的这种方式有一个好处就是获取的图片是拍照后的原图
         * 如果不实用ContentValues存放照片路径的话，拍照后获取的图片为缩略图不清晰
         */
        ContentValues values = new ContentValues();
        Uri photoUri = fragment.getActivity().getContentResolver()
                .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoUri);
        fragment.startActivityForResult(intent, REQUEST_CODE_TAKE_PHOTO);
        return photoUri;
    }

    public static void takeVideo(android.support.v4.app.Fragment fragment) {
        Intent intent = takeVideoIntent();
        fragment.startActivityForResult(intent, REQUEST_CODE_TAKE_VIDEO);
    }

    /**
     * 从相册中取图片
     */
    public static void pickPhoto(android.support.v4.app.Fragment fragment) {
        Intent intent = pickPhotoIntent();
        fragment.startActivityForResult(intent, REQUEST_CODE_PICK_PHOTO);
    }

    public static void pickVideo(android.support.v4.app.Fragment fragment) {
        Intent intent = pickVideoIntent();
        fragment.startActivityForResult(intent, REQUEST_CODE_PICK_VIDEO);
    }

    // ////////////////////////////////

    public static void cropImage(Uri uri, int width, int height,
                                 Activity activity) {
        Intent intent = cropImageIntent(uri, width, height);
        activity.startActivityForResult(intent, REQUEST_CODE_CROP);
    }

    public static void cropImage(Uri uri, int width, int height,
                                 android.support.v4.app.Fragment fragment) {
        Intent intent = cropImageIntent(uri, width, height);
        fragment.startActivityForResult(intent, REQUEST_CODE_CROP);
    }

    public static Uri gainImageUri(int requestCode, Intent data, Uri takeUri) {
        Uri photoUri = null;
        if (requestCode == CameraUtil.REQUEST_CODE_TAKE_PHOTO) {
            if (data != null) {
                photoUri = data.getData();
            }
            if (photoUri == null) {
                photoUri = takeUri;
            }
        } else if (requestCode == CameraUtil.REQUEST_CODE_PICK_PHOTO) {
            if (data != null) {
                photoUri = data.getData();
            }
        } else if (requestCode == CameraUtil.REQUEST_CODE_CROP) {
            // picdata.getExtras(), Bitmap photo = bundle.getParcelable("data");
            // Bitmap photo1 = data.getParcelableExtra("data");
            // TODO 需要测试
        }
        return photoUri;
    }
}
