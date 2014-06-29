package com.roots.tools;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Enumeration;
import java.util.Properties;

/**
 *
 * @author seetharamanj
 */
public class VCFParser
{
    public static void main(String[] args) throws Exception
    {        
    	int i = 0;
        //if(args.length==1)
        {
            //File dir=new File(args[0]);
            File dir=new File("C:\\MyDocs\\Downloads\\vCardOrganizer2\\vCardOrganizer\\data");
            if(dir.isDirectory())
            {
                String[] files=dir.list();
                for(String file:files)
                {
                    Properties prop=new Properties();
                    prop.load(new FileReader(dir+"/"+file));
                    Enumeration keys=prop.keys();
                    String contact="";
                    while(keys.hasMoreElements())
                    {                
                        String key=keys.nextElement().toString();
                        if(key.indexOf("N;")!=-1)
                        {
                            String val=prop.getProperty(key);
                            contact=val.substring(val.indexOf(":")+1);
                        }
                        else if(key.indexOf("TEL;")!=-1)
                        {
                            String val=prop.getProperty(key);
                            contact=contact+" : "+val;
                        }                
                    }
                    System.out.println(contact);
                    i++;
                    if(i>10)break;
                }//for
            }//isDir
        }//args
    //else
        System.out.println("Usage: java VCFParser <dirPathWhichContainsVCFFiles>");
    }//args    
}
