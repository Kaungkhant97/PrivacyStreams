package io.github.privacystreams.image;

import android.Manifest;
import android.content.Context;
import android.graphics.Bitmap;

import io.github.privacystreams.core.PStreamProvider;
import io.github.privacystreams.core.exceptions.PSException;
import io.github.privacystreams.utils.Logging;
import io.github.privacystreams.utils.PermissionUtils;
import io.github.privacystreams.utils.TimeUtils;

/**
 * Take a photo in the background.
 */

class BackgroundPhotoProvider extends PStreamProvider {
    private static final String TAG = "BackgroundPhotoProvider";

    private final int cameraId;

    BackgroundPhotoProvider(int cameraId) {
        this.cameraId = cameraId;
        this.addParameters(cameraId);
        this.addRequiredPermissions(Manifest.permission.CAMERA);
//        this.addRequiredPermissions(Manifest.permission.SYSTEM_ALERT_WINDOW);
        this.addRequiredPermissions(PermissionUtils.OVERLAY);
    }

    @Override
    protected void provide() {
        Context ctx = this.getUQI().getContext();
        PSCameraBgService.takePhoto(ctx, cameraId, mCameraCallback);
    }

    private void stop() {
        Context ctx = this.getUQI().getContext();
        PSCameraBgService.stopTakingPhoto(ctx, mCameraCallback);
    }

    private PSCameraBgService.Callback mCameraCallback = new PSCameraBgService.Callback() {
        @Override
        void onImageTaken(byte[] imageBytes) {
            ImageData imageData = ImageData.newTempImage(imageBytes);
            Image imageItem = new Image(TimeUtils.now(), imageData);
            output(imageItem);
            stop();
            finish();
        }

        @Override
        void onFail(boolean isFatal, String errorMessage) {
            Logging.error(TAG + ": " + (isFatal? "FATAL" : "") + errorMessage);
            stop();
            raiseException(getUQI(), PSException.FAILED(errorMessage));
        }
    };
}
