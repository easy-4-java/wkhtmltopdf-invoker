package com.github.jhonnymertz.Calibre.wrapper.configurations;

import java.io.IOException;
import java.nio.charset.Charset;
import org.apache.commons.io.IOUtils;

public class WrapperConfig {

    private XvfbConfig xvfbConfig;

    private String CalibreCommand = "Calibre";

    public WrapperConfig() {
      setCalibreCommand(findExecutable());
    }

    public WrapperConfig(String CalibreCommand) {
        setCalibreCommand(CalibreCommand);
    }

    public String getCalibreCommand() {
        return CalibreCommand;
    }

    public void setCalibreCommand(String CalibreCommand) {
        this.CalibreCommand = CalibreCommand;
    }

    /**
     * Attempts to find the `Calibre` executable in the system path.
     *
     * @return the Calibre command according to the OS
     */
    public String findExecutable() {
        try {
        	
            String osname = System.getProperty("os.name").toLowerCase();

            String cmd = osname.contains("windows") ? "where Calibre" : "which Calibre";

            Process p = Runtime.getRuntime().exec(cmd);
            p.waitFor();

            String text = IOUtils.toString(p.getInputStream(), Charset.defaultCharset()).trim();

            if (text.isEmpty())
                throw new RuntimeException();

            setCalibreCommand(text);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return getCalibreCommand();
    }

    public boolean isXvfbEnabled() {
        return xvfbConfig != null;
    }

    public XvfbConfig getXvfbConfig() {
        return xvfbConfig;
    }

    public void setXvfbConfig(XvfbConfig xvfbConfig) {
        this.xvfbConfig = xvfbConfig;
    }
}
