package osrs;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import javax.net.ssl.HandshakeCompletedListener;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import org.bouncycastle.crypto.tls.TlsClientProtocol;

public class cB extends SSLSocketFactory {
   public SecureRandom a = new SecureRandom();

   public Socket createSocket(Socket var1, String var2, int var3, boolean var4) throws IOException {
      if (var1 == null) {
         var1 = new Socket();
      }

      if (!var1.isConnected()) {
         var1.connect(new InetSocketAddress(var2, var3));
      }

      TlsClientProtocol var5 = new TlsClientProtocol(var1.getInputStream(), var1.getOutputStream(), this.a);
      return this.a(var2, var5);
   }

   public String[] getDefaultCipherSuites() {
      return null;
   }

   public String[] getSupportedCipherSuites() {
      return null;
   }

   public Socket createSocket(String var1, int var2) throws IOException, UnknownHostException {
      return null;
   }

   public Socket createSocket(InetAddress var1, int var2) throws IOException {
      return null;
   }

   public Socket createSocket(String var1, int var2, InetAddress var3, int var4) throws IOException, UnknownHostException {
      return null;
   }

   public Socket createSocket(InetAddress var1, int var2, InetAddress var3, int var4) throws IOException {
      return null;
   }

   public SSLSocket a(String var1, TlsClientProtocol var2) {
      return new a(var2, var1);
   }

   class a extends SSLSocket {
      public final TlsClientProtocol a;
      public final String b;
      public Certificate[] c;

      public a(TlsClientProtocol var2, String var3) {
         this.a = var2;
         this.b = var3;
      }

      public InputStream getInputStream() throws IOException {
         return this.a.getInputStream();
      }

      public OutputStream getOutputStream() throws IOException {
         return this.a.getOutputStream();
      }

      public synchronized void close() throws IOException {
         this.a.close();
      }

      public void addHandshakeCompletedListener(HandshakeCompletedListener var1) {
      }

      public boolean getEnableSessionCreation() {
         return false;
      }

      public void setEnableSessionCreation(boolean var1) {
      }

      public String[] getEnabledCipherSuites() {
         return null;
      }

      public void setEnabledCipherSuites(String[] var1) {
      }

      public String[] getEnabledProtocols() {
         return null;
      }

      public void setEnabledProtocols(String[] var1) {
      }

      public boolean getNeedClientAuth() {
         return false;
      }

      public void setNeedClientAuth(boolean var1) {
      }

      public SSLSession getSession() {
         return new dI(this);
      }

      public String[] getSupportedProtocols() {
         return null;
      }

      public String[] getSupportedCipherSuites() {
         return null;
      }

      public boolean getUseClientMode() {
         return false;
      }

      public void setUseClientMode(boolean var1) {
      }

      public boolean getWantClientAuth() {
         return false;
      }

      public void setWantClientAuth(boolean var1) {
      }

      public void removeHandshakeCompletedListener(HandshakeCompletedListener var1) {
      }

      public void startHandshake() throws IOException {
         this.a.connect(new hT(this));
      }
   }
}
