package com.example.antoine.HandleXML;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by antoine on 23/01/2017.
 */

public class HandleXMLSaintJour {
        private String saint = "";
        private String urlString=null;
        private XmlPullParserFactory xmlFactoryObject;
        public volatile boolean parsingComplete = true;
        private String info=null;
        private boolean isBody = false;

        public HandleXMLSaintJour(String url){
            this.urlString = url;
        }

        public String getInfo(){
            return this.info;
        }

        public String getSaint(){
            return this.saint;
        }

        public void parseXMLAndStoreIt(XmlPullParser myParser) {
            int event;
            String text=null;

            try {
                event = myParser.getEventType();

                while (event != XmlPullParser.END_DOCUMENT) {
                    String name=myParser.getName();

                    switch (event){
                        case XmlPullParser.START_TAG:
                            if (name.equalsIgnoreCase("description")) {
                                // create a new instance of employee
                                info="";
                            } else if(name.equalsIgnoreCase("item")){
                                isBody = true;
                            }
                            break;

                        case XmlPullParser.TEXT:
                            text = myParser.getText();
                            break;

                        case XmlPullParser.END_TAG:

                            if(name.equals("item")) {
                                isBody = false;
                            }

                            else if(name.equals("description") && isBody){
                                if(text.contains("<BR"))
                                    text = text.substring(0, text.indexOf("<BR"));
                                saint = text.substring(text.indexOf("<b>") + "<b>".length(),text.indexOf("</b>"));
                                info=text.substring(text.indexOf("</b>") + "</b>".length());
                            }

                            else{
                            }

                            break;
                    }

                    event = myParser.next();
                }

                parsingComplete = false;
            }

            catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void fetchXML(){
            Thread thread = new Thread(new Runnable(){
                @Override
                public void run() {

                    try {
                        URL url = new URL(urlString);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                        conn.setReadTimeout(1000 /* milliseconds */);
                        conn.setConnectTimeout(1000 /* milliseconds */);
                        conn.setRequestMethod("GET");
                        conn.setDoInput(true);

                        // Starts the query
                        conn.connect();
                        InputStream stream = conn.getInputStream();

                        xmlFactoryObject = XmlPullParserFactory.newInstance();
                        XmlPullParser myparser = xmlFactoryObject.newPullParser();

                        myparser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                        myparser.setInput(stream, null);

                        parseXMLAndStoreIt(myparser);
                        stream.close();
                    }

                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
        }
}
