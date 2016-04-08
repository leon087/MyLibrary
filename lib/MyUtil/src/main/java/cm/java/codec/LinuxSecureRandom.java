//package cm.java.codec;
//
//public class LinuxSecureRandom extends SecureRandomSpi implements Serializable {
//
//    private static final long serialVersionUID = -6445246205931913678L;
//
//    private static FileInputStream urandom;
//
//    private final DataInputStream dis = new DataInputStream(urandom);
//
//    static {
//        try {
//            File localFile = new File("/dev/urandom");
//            if (localFile.exists()) {
//                urandom = new FileInputStream(localFile);
//                Security.insertProviderAt(new LinuxSecureRandomProvider(), 1);
//            } else {
//                urandom = null;
//            }
//        } catch (FileNotFoundException localFileNotFoundException) {
//            throw new RuntimeException(localFileNotFoundException);
//        }
//    }
//
//    protected byte[] engineGenerateSeed(int paramInt) {
//        byte[] arrayOfByte = new byte[paramInt];
//        engineNextBytes(arrayOfByte);
//        return arrayOfByte;
//    }
//
//    protected void engineNextBytes(byte[] paramArrayOfByte) {
//        try {
//            this.dis.readFully(paramArrayOfByte);
//        } catch (IOException localIOException) {
//            throw new RuntimeException(localIOException);
//        }
//    }
//
//    protected void engineSetSeed(byte[] paramArrayOfByte) {
//    }
//
//    private static class LinuxSecureRandomProvider extends Provider {
//
//        public LinuxSecureRandomProvider() {
//            super("LinuxSecureRandomProvider", 1.0D,
//                    "A Linux specific random number provider that uses /dev/urandom");
//            put("SecureRandom.LinuxSecureRandom", LinuxSecureRandom.class.getName());
//        }
//    }
//}