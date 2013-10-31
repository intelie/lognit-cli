package net.intelie.lognit.cli.http;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class UnsafeSSLSocketFactory implements ProtocolSocketFactory {
    private SSLContext sslContext = SSLContext.getInstance("TLS");

    public UnsafeSSLSocketFactory() throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
        TrustManager tm = new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };

        sslContext.init(null, new TrustManager[]{tm}, null);
    }

    @Override
    public Socket createSocket(String host, int port, InetAddress localAddress, int localPort) throws IOException, UnknownHostException {
        return sslContext.getSocketFactory().createSocket(host, port, localAddress, localPort);
    }

    @Override
    public Socket createSocket(String host, int port, InetAddress localAddress, int localPort, HttpConnectionParams params) throws IOException, UnknownHostException, ConnectTimeoutException {
        return sslContext.getSocketFactory().createSocket(host, port, localAddress, localPort);
    }

    @Override
    public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
        return sslContext.getSocketFactory().createSocket(host, port);
    }
}
