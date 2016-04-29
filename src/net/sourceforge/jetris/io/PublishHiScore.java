/*
 * Decompiled with CFR 0_114.
 */
package net.sourceforge.jetris.io;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import net.sourceforge.jetris.io.HiScore;
import java.util.Base64;
//import sun.misc.BASE64Encoder;
//import org.apache.commons.codec.binary.Base64;

public class PublishHiScore {
    private static final String URL = "http://jetris.sourceforge.net";
    //private static final BASE64Encoder B64E = new BASE64Encoder();
    private static final Base64.Encoder B64E = Base64.getEncoder();
    static void publish(HiScore hs) throws Exception {
        if (hs.score == 0) {
            return;
        }
        StringBuffer sb = new StringBuffer(100);
        sb.append("http://jetris.sourceforge.net");
        sb.append("/update.php?pass=26195&sc=");
        sb.append(hs.score);
        sb.append("&ln=");
        sb.append(hs.lines);
        sb.append("&name=");
        if (hs.name != null && hs.name.compareTo("") != 0) {
            sb.append(B64E.encode(hs.name.getBytes()));
        } else {
            sb.append(B64E.encode("<empty>".getBytes()));
        }
        BufferedReader in = new BufferedReader(new InputStreamReader(new URL(sb.toString()).openStream()));
        boolean b = false;
        if ((char)in.read() != 'o') {
            b = true;
        }
        if ((char)in.read() != 'k') {
            b = true;
        }
        in.close();
        if (b) {
            throw new Exception("Could not update");
        }
    }
}