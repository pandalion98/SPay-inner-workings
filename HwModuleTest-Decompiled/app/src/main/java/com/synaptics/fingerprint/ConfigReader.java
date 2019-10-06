package com.synaptics.fingerprint;

import android.util.Log;
import com.goodix.cap.fingerprint.utils.TestResultParser;
import com.sec.android.app.hwmoduletest.modules.ModulePower;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public class ConfigReader {
    public static final String CONFIG_FILE = "/system/etc/SynapticsConfig.xml";
    public static final String TAG = "ConfigReader";

    public static class ConfigData {
        public String disableButton;
        public DisableButtons disableButtons;
        public String fingerActionGenericLabel;
        public String fingerLiftLabel;
        public String fingerPlaceOrSwipeLabel;
        public FingerSwipeAnimation fingerSwipeAnimation;
        public FpDisplay fpDisplay;
        public boolean hapticFeedback;
        public boolean practiceMode;
        public int screenOrientation;
        public SensorBar sensorBar;
        public int sensorType;
        public boolean showVideo;

        public class DisableButtons {
            public boolean back = false;
            public boolean home = false;
            public boolean menu = false;
            public boolean search = false;

            public DisableButtons() {
            }
        }

        class FingerSwipeAnimation {
            int animationSpeed = ModulePower.BUTTON_KEY_LIGHT_ON_1500;
            int offsetLength = 0;
            int orientation = 0;
            boolean outlineVisible = true;
            boolean visible = false;
            int xPos = TestResultParser.TEST_TOKEN_RESET_FLAG;
            float xScale = 1.0f;
            int yPos = 800;
            float yScale = 1.0f;

            FingerSwipeAnimation() {
            }
        }

        public class FpDisplay {
            public int height = TestResultParser.TEST_TOKEN_GET_DR_TIMESTAMP_TIME;
            public boolean showStartupVideo = false;
            public int width = 200;
            public int xPos = 0;
            public int yPos = 0;

            public FpDisplay() {
            }
        }

        class SensorBar {
            int height = 100;
            boolean visible = false;
            int width = 25;
            int xPos = TestResultParser.TEST_TOKEN_RESET_FLAG;
            int yPos = 800;

            SensorBar() {
            }
        }

        ConfigData() {
            this.screenOrientation = 0;
            this.sensorType = 1;
            this.disableButton = "0000";
            this.showVideo = false;
            this.practiceMode = false;
            this.hapticFeedback = true;
            this.fingerActionGenericLabel = "Please swipe";
            this.fingerPlaceOrSwipeLabel = "Place finger";
            this.fingerLiftLabel = "Please lift your finger";
            this.sensorBar = null;
            this.fingerSwipeAnimation = null;
            this.disableButtons = null;
            this.fpDisplay = null;
            this.sensorBar = new SensorBar();
            this.fingerSwipeAnimation = new FingerSwipeAnimation();
            this.fpDisplay = new FpDisplay();
            this.disableButtons = new DisableButtons();
        }

        public void toStringDebug() {
            String str = ConfigReader.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("ConfigData():: Screen Orientation=");
            sb.append(this.screenOrientation);
            sb.append(", SensorType=");
            sb.append(this.sensorType);
            sb.append(", disabled buttons=");
            sb.append(this.disableButton);
            sb.append(", fingerPlaceOrSwipeLabel=");
            sb.append(this.fingerPlaceOrSwipeLabel);
            sb.append(", fingerLiftLabel=");
            sb.append(this.fingerLiftLabel);
            sb.append(", fingerActionGenericLabel=");
            sb.append(this.fingerActionGenericLabel);
            sb.append(", showVideo=");
            sb.append(this.showVideo);
            sb.append(", practiceMode=");
            sb.append(this.practiceMode);
            sb.append(", hapticFeedback=");
            sb.append(this.hapticFeedback);
            Log.i(str, sb.toString());
            String str2 = ConfigReader.TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Fp Display :: left = ");
            sb2.append(this.fpDisplay.xPos);
            sb2.append(", top = ");
            sb2.append(this.fpDisplay.yPos);
            sb2.append(", width = ");
            sb2.append(this.fpDisplay.width);
            sb2.append(", height = ");
            sb2.append(this.fpDisplay.height);
            Log.i(str2, sb2.toString());
            String str3 = ConfigReader.TAG;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("DisableButtons =>  home=");
            sb3.append(this.disableButtons.home);
            sb3.append(", menu=");
            sb3.append(this.disableButtons.menu);
            sb3.append(", search=");
            sb3.append(this.disableButtons.search);
            sb3.append(", back=");
            sb3.append(this.disableButtons.back);
            Log.i(str3, sb3.toString());
            String str4 = ConfigReader.TAG;
            StringBuilder sb4 = new StringBuilder();
            sb4.append("Finger swipe animation =>  visible=");
            sb4.append(this.fingerSwipeAnimation.visible);
            sb4.append(", orientation=");
            sb4.append(this.fingerSwipeAnimation.orientation);
            sb4.append(", left=");
            sb4.append(this.fingerSwipeAnimation.xPos);
            sb4.append(", top=");
            sb4.append(this.fingerSwipeAnimation.yPos);
            sb4.append(", scale width = ");
            sb4.append(this.fingerSwipeAnimation.xScale);
            sb4.append(", scale height = ");
            sb4.append(this.fingerSwipeAnimation.yScale);
            sb4.append(", offset length = ");
            sb4.append(this.fingerSwipeAnimation.offsetLength);
            sb4.append(", outline visible = ");
            sb4.append(this.fingerSwipeAnimation.outlineVisible);
            sb4.append(", animationSpeed = ");
            sb4.append(this.fingerSwipeAnimation.animationSpeed);
            Log.i(str4, sb4.toString());
            String str5 = ConfigReader.TAG;
            StringBuilder sb5 = new StringBuilder();
            sb5.append("Fp Display :: left = ");
            sb5.append(this.fpDisplay.xPos);
            sb5.append(", top = ");
            sb5.append(this.fpDisplay.yPos);
            sb5.append(", width = ");
            sb5.append(this.fpDisplay.width);
            sb5.append(", height = ");
            sb5.append(this.fpDisplay.height);
            sb5.append(", showStartupVideo = ");
            sb5.append(this.fpDisplay.showStartupVideo);
            Log.i(str5, sb5.toString());
        }
    }

    public static class DataHandler extends DefaultHandler {
        private ConfigData _data;
        private boolean _disableButtons;
        private boolean _fingerActionGenericLabel;
        private boolean _fingerLiftLabel;
        private boolean _fingerPlaceOrSwipeLabel;
        private boolean _fingerSwipeAnimation;
        private boolean _fpDisplay;
        private boolean _hapticFeedback;
        private boolean _practiceMode;
        private boolean _screenOrientation;
        private boolean _sensorBar;
        private boolean _sensorType;
        private boolean _showVideo;

        public ConfigData getData() {
            return this._data;
        }

        public void startDocument() throws SAXException {
            this._data = new ConfigData();
        }

        public void endDocument() throws SAXException {
        }

        public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
            boolean z = true;
            if (localName.equals("screenOrientation")) {
                this._screenOrientation = true;
            } else if (localName.equals("sensorType")) {
                this._sensorType = true;
            } else if (localName.equals("showVideo")) {
                this._showVideo = true;
            } else if (localName.equals("practiceMode")) {
                this._practiceMode = true;
            } else if (localName.equals("hapticFeedback")) {
                this._hapticFeedback = true;
            } else if (localName.equals("fingerPlaceOrSwipeLabel")) {
                this._fingerPlaceOrSwipeLabel = true;
            } else if (localName.equals("fingerLiftLabel")) {
                this._fingerLiftLabel = true;
            } else if (localName.equals("fingerActionGenericLabel")) {
                this._fingerActionGenericLabel = true;
            } else if (localName.equals("disableButtons")) {
                this._disableButtons = true;
            } else if (localName.equals("sensorBar")) {
                this._sensorBar = true;
                if (atts.getIndex("visible") != -1) {
                    SensorBar sensorBar = this._data.sensorBar;
                    if (Integer.parseInt(atts.getValue("visible")) != 1) {
                        z = false;
                    }
                    sensorBar.visible = z;
                }
                if (atts.getIndex("xPos") != -1) {
                    this._data.sensorBar.xPos = Integer.parseInt(atts.getValue("xPos"));
                }
                if (atts.getIndex("yPos") != -1) {
                    this._data.sensorBar.yPos = Integer.parseInt(atts.getValue("yPos"));
                }
                if (atts.getIndex("width") != -1) {
                    this._data.sensorBar.width = Integer.parseInt(atts.getValue("width"));
                }
                if (atts.getIndex("height") != -1) {
                    this._data.sensorBar.height = Integer.parseInt(atts.getValue("height"));
                }
            } else if (localName.equals("fingerSwipeAnimation")) {
                this._fingerSwipeAnimation = true;
                if (atts.getIndex("visible") != -1) {
                    this._data.fingerSwipeAnimation.visible = Integer.parseInt(atts.getValue("visible")) == 1;
                }
                if (atts.getIndex("orientation") != -1) {
                    this._data.fingerSwipeAnimation.orientation = Integer.parseInt(atts.getValue("orientation"));
                }
                if (atts.getIndex("xPos") != -1) {
                    this._data.fingerSwipeAnimation.xPos = Integer.parseInt(atts.getValue("xPos"));
                }
                if (atts.getIndex("yPos") != -1) {
                    this._data.fingerSwipeAnimation.yPos = Integer.parseInt(atts.getValue("yPos"));
                }
                if (atts.getIndex("xScale") != -1) {
                    this._data.fingerSwipeAnimation.xScale = Float.parseFloat(atts.getValue("xScale"));
                }
                if (atts.getIndex("yScale") != -1) {
                    this._data.fingerSwipeAnimation.yScale = Float.parseFloat(atts.getValue("yScale"));
                }
                if (atts.getIndex("offsetLength") != -1) {
                    this._data.fingerSwipeAnimation.offsetLength = Integer.parseInt(atts.getValue("offsetLength"));
                }
                if (atts.getIndex("outlineVisible") != -1) {
                    FingerSwipeAnimation fingerSwipeAnimation = this._data.fingerSwipeAnimation;
                    if (Integer.parseInt(atts.getValue("outlineVisible")) != 1) {
                        z = false;
                    }
                    fingerSwipeAnimation.outlineVisible = z;
                }
                if (atts.getIndex("animationSpeed") != -1) {
                    this._data.fingerSwipeAnimation.animationSpeed = Integer.parseInt(atts.getValue("animationSpeed"));
                }
            } else if (localName.equals("fpDisplay")) {
                this._fpDisplay = true;
                if (atts.getIndex("xPos") != -1) {
                    this._data.fpDisplay.xPos = Integer.parseInt(atts.getValue("xPos"));
                }
                if (atts.getIndex("yPos") != -1) {
                    this._data.fpDisplay.xPos = Integer.parseInt(atts.getValue("yPos"));
                }
                if (atts.getIndex("width") != -1) {
                    this._data.fpDisplay.width = Integer.parseInt(atts.getValue("width"));
                }
                if (atts.getIndex("height") != -1) {
                    this._data.fpDisplay.height = Integer.parseInt(atts.getValue("height"));
                }
                if (atts.getIndex("showStartupVideo") != -1) {
                    FpDisplay fpDisplay = this._data.fpDisplay;
                    if (Integer.parseInt(atts.getValue("showStartupVideo")) != 1) {
                        z = false;
                    }
                    fpDisplay.showStartupVideo = z;
                }
            }
        }

        public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
            if (localName.equals("screenOrientation")) {
                this._screenOrientation = false;
            } else if (localName.equals("sensorType")) {
                this._sensorType = false;
            } else if (localName.equals("showVideo")) {
                this._showVideo = false;
            } else if (localName.equals("practiceMode")) {
                this._practiceMode = false;
            } else if (localName.equals("hapticFeedback")) {
                this._hapticFeedback = false;
            } else if (localName.equals("fingerPlaceOrSwipeLabel")) {
                this._fingerPlaceOrSwipeLabel = false;
            } else if (localName.equals("fingerLiftLabel")) {
                this._fingerLiftLabel = false;
            } else if (localName.equals("fingerActionGenericLabel")) {
                this._fingerActionGenericLabel = false;
            } else if (localName.equals("disableButtons")) {
                this._disableButtons = false;
            } else if (localName.equals("sensorBar")) {
                this._sensorBar = false;
            } else if (localName.equals("fingerSwipeAnimation")) {
                this._fingerSwipeAnimation = false;
            } else if (localName.equals("fpDisplay")) {
                this._fpDisplay = false;
            }
        }

        public void characters(char[] ch, int start, int length) {
            String chars = new String(ch, start, length).trim();
            if (this._screenOrientation) {
                this._data.screenOrientation = Integer.parseInt(chars);
            } else if (this._sensorType) {
                this._data.sensorType = Integer.parseInt(chars);
            } else if (this._showVideo) {
                this._data.showVideo = Boolean.parseBoolean(chars);
            } else if (this._practiceMode) {
                this._data.practiceMode = Boolean.parseBoolean(chars);
            } else if (this._hapticFeedback) {
                this._data.hapticFeedback = Boolean.parseBoolean(chars);
            } else if (this._fingerPlaceOrSwipeLabel) {
                this._data.fingerPlaceOrSwipeLabel = chars;
            } else if (this._fingerLiftLabel) {
                this._data.fingerLiftLabel = chars;
            } else if (this._fingerActionGenericLabel) {
                this._data.fingerActionGenericLabel = chars;
            } else if (this._disableButtons) {
                this._data.disableButton = chars;
                boolean z = false;
                this._data.disableButtons.search = ch[length + -1] == '1';
                this._data.disableButtons.home = ch[length + -2] == '1';
                this._data.disableButtons.back = ch[length + -3] == '1';
                DisableButtons disableButtons = this._data.disableButtons;
                if (ch[length - 4] == '1') {
                    z = true;
                }
                disableButtons.menu = z;
            } else if (!this._sensorBar && !this._fingerSwipeAnimation) {
                boolean z2 = this._fpDisplay;
            }
        }
    }

    public static ConfigData getData() {
        ConfigData configData = null;
        try {
            InputStream inputStream = new FileInputStream(CONFIG_FILE);
            if (inputStream.available() > 0) {
                DataHandler dataHandler = new DataHandler();
                XMLReader xr = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
                xr.setContentHandler(dataHandler);
                xr.parse(new InputSource(inputStream));
                configData = dataHandler.getData();
            }
            inputStream.close();
        } catch (FileNotFoundException e) {
            Log.e(TAG, "Configuration file[/system/etc/SynapticsConfig.xml] not found");
        } catch (ParserConfigurationException pce) {
            Log.e(TAG, "SAX parse error", pce);
        } catch (SAXException se) {
            Log.e(TAG, "SAX error", se);
        } catch (IOException ioe) {
            Log.e(TAG, "SAX parse io error", ioe);
        }
        return configData;
    }
}
