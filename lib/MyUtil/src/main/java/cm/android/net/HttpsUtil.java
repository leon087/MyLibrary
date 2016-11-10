package cm.android.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.Arrays;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import cm.java.util.IoUtil;

/**
 * The type Https util.
 */
public class HttpsUtil {
    private static final Logger logger = LoggerFactory.getLogger("https");

    /**
     * Read cert certificate.
     *
     * @param is the is
     * @return the certificate
     */
    public static Certificate readCert(InputStream is) {
        CertificateFactory cf = null;
        try {
            cf = CertificateFactory.getInstance("X.509");
            return cf.generateCertificate(is);
        } catch (CertificateException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
//        Log.i("Longer", "ca=" + ((X509Certificate) ca).getSubjectDN());
//        Log.i("Longer", "key=" + ((X509Certificate) ca).getPublicKey());
    }

    public static TrustManagerFactory createTrustManagerFactory(Context context, KeyStoreConfig config)
            throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException {
        InputStream trustIs = null;
        try {
            trustIs = context.getResources().getAssets().open(config.fileName);
//        String type = KeyStore.getDefaultType();
            KeyStore trust = KeyStore.getInstance(config.type);
            trust.load(trustIs, config.pwd.toCharArray());
            //        keyStore.setCertificateEntry("ca", cert);

            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(trust);
            return tmf;
        } finally {
            IoUtil.closeQuietly(trustIs);
        }
    }

    public static final class KeyStoreConfig {
        public String fileName;
        public String type;
        public String pwd;
    }

    public static KeyManagerFactory createKeyManagerFactory(Context context, KeyStoreConfig config)
            throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException, UnrecoverableKeyException {
        InputStream clientIs = null;
        try {
            clientIs = context.getResources().getAssets().open(config.fileName);
//        String type = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(config.type);
            keyStore.load(clientIs, config.pwd.toCharArray());

//        String algorithm = KeyManagerFactory.getDefaultAlgorithm();
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("X509");
            keyManagerFactory.init(keyStore, config.pwd.toCharArray());
            return keyManagerFactory;
        } finally {
            IoUtil.closeQuietly(clientIs);
        }
    }

    public static TrustManagerFactory createTrustManagerFactory(Context context, String crtFileName)
            throws IOException, KeyStoreException, CertificateException,
            NoSuchAlgorithmException {
        InputStream trustIs = null;
        try {
            trustIs = context.getResources().getAssets().open(crtFileName);
            Certificate certificate = readCert(trustIs);
            if (certificate == null) {
                return null;
            }
            KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
            keystore.load(null, null);
            keystore.setCertificateEntry("ca", certificate);

            TrustManagerFactory tmf = TrustManagerFactory.getInstance(
                    TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(keystore);
            return tmf;
        } finally {
            IoUtil.closeQuietly(trustIs);
        }
    }

    /**
     * Gets ssl context.
     *
     * @param tmf the tmf
     * @param kmf the kmf
     * @return the ssl context
     * @throws NoSuchAlgorithmException the no such algorithm exception
     * @throws KeyManagementException   the key management exception
     */
    public static SSLContext getSSLContext(TrustManagerFactory tmf, KeyManagerFactory kmf) throws NoSuchAlgorithmException, KeyManagementException {
        TrustManager[] trustManagers = tmf.getTrustManagers();
        if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
            throw new IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustManagers));
        }

        SSLContext sslContext = SSLContext.getInstance("TLS");
//        SSLContext context = SSLContext.getInstance("TLSv1","AndroidOpenSSL");
        KeyManager[] km = null;
        if (kmf != null) {
            km = kmf.getKeyManagers();
        }
        sslContext.init(km, trustManagers, new SecureRandom());
        return sslContext;
    }

    public static void setDefaultSSLSocketFactory(TrustManagerFactory tmf, KeyManagerFactory kmf) throws KeyManagementException, NoSuchAlgorithmException {
        SSLContext sc = HttpsUtil.getSSLContext(tmf, kmf);
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    }
}
