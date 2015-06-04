package cm.java.codec;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.Provider;
import java.security.SecureRandomSpi;
import java.security.Security;

public class LinuxSecureRandom extends SecureRandomSpi {

    private static FileInputStream urandom;

    private final DataInputStream dis = new DataInputStream(urandom);

    static {
        try {
            File localFile = new File("/dev/urandom");
            if (localFile.exists()) {
                urandom = new FileInputStream(localFile);
                Security.insertProviderAt(new LinuxSecureRandomProvider(), 1);
            } else {
                urandom = null;
            }
        } catch (FileNotFoundException localFileNotFoundException) {
            throw new RuntimeException(localFileNotFoundException);
        }
    }

    protected byte[] engineGenerateSeed(int paramInt) {
        byte[] arrayOfByte = new byte[paramInt];
        engineNextBytes(arrayOfByte);
        return arrayOfByte;
    }

    protected void engineNextBytes(byte[] paramArrayOfByte) {
        try {
            this.dis.readFully(paramArrayOfByte);
        } catch (IOException localIOException) {
            throw new RuntimeException(localIOException);
        }
    }

    protected void engineSetSeed(byte[] paramArrayOfByte) {
    }

    private static class LinuxSecureRandomProvider extends Provider {

        public LinuxSecureRandomProvider() {
            super("LinuxSecureRandomProvider", 1.0D,
                    "A Linux specific random number provider that uses /dev/urandom");
            put("SecureRandom.LinuxSecureRandom", LinuxSecureRandom.class.getName());
        }
    }
}