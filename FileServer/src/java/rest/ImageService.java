/* Copyright (C) 2014 Grigorios G. Chrysos
   available under the terms of the Apache License, Version 2.0 */
package rest;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
/*import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
*/
import com.sun.jersey.core.header.FormDataContentDisposition; 
import com.sun.jersey.multipart.FormDataParam; 
import java.net.URI;
import java.nio.file.LinkOption;
import java.util.Random;


@Path("images")
public class ImageService {
    /* greg, August 2013: 3 declarations below are for xmlParser */ 
   /* private DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
    private DocumentBuilder builder = null;
    private XPath xPath =  XPathFactory.newInstance().newXPath();
*/
    /*
     * The maximum allowed file size in megabytes.
     * Files larger than this size will not be uploadable or downloadable.
     */
    private static final int MAX_SIZE_IN_MB = 5;
    
    /*
     * The directory where the images will be stored.
     * Make sure this directory exists before you run the service.
     */
    private static final String FINAL_DIR_STR="/home/user/Pictures/FinalDest/";
    private static final String BASE_DIR_STR="/home/user/Pictures/SavedImages/";
    private static final String MAIL_DIR_STR="/home/user/NetBeansProjects/FileServer/build/web/uploads/";
    //private static final java.nio.file.Path BASE_DIR = Paths.get(System.getProperty("user.home"), "Pictures", "SavedImages");
    private static final java.nio.file.Path BASE_DIR = Paths.get(BASE_DIR_STR);
    //private static final java.nio.file.Path FINAL_DIR = Paths.get(System.getProperty("user.home"), "Pictures", "FinalDest");
    private static final java.nio.file.Path FINAL_DIR = Paths.get(FINAL_DIR_STR);
    private String fileName;
    
    
    @GET
    @Produces("text/plain")
    public String sendLink(){
        System.out.println("hey greg, we are in GET :D ");
        String str=fileName;
        return str;
    }       
    

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces("text/plain")
    public String uploadData( 
            @FormDataParam("file") InputStream uploadedInputStream,
            @FormDataParam("file") FormDataContentDisposition fileDetail,
            @FormDataParam("description") String fileType,
            @FormDataParam("category") String category) throws IOException {
        
        System.out.println("multipart "+category);
        String extensionFL, initialFL;
       long fileSize=fileDetail.getSize();//String fileType="png";
        // Make sure the file is not larger than the maximum allowed size.
        if (fileSize > 1024 * 1024 * MAX_SIZE_IN_MB) {
            throw new WebApplicationException(Response.status(Status.BAD_REQUEST).entity("Image is larger than " + MAX_SIZE_IN_MB + "MB").build());
        }
        Random randomGenerator = new Random();
        int randomInt = randomGenerator.nextInt(1000);
        // Generate a random file name based on the current time.
        fileName = ""+randomInt + System.currentTimeMillis();
        initialFL=fileName;
        if (fileType.equals("image/jpeg")) {
            fileName += ".jpg";extensionFL="jpg";
        } else if (fileType.equals("image/png")) {
            //System.out.println("png");
            fileName += ".png";extensionFL="png";
        }else if (fileType.equals("image/bmp")){
            fileName += ".bmp";extensionFL="bmp";
        }else{
            throw new WebApplicationException(Response.status(Status.BAD_REQUEST).entity("Unsupported file type ").build());
        }
        
        // Copy the file to its location
        java.nio.file.Path BASE_DIR2=Paths.get(BASE_DIR_STR+category+"/");
        if (Files.exists(BASE_DIR2,LinkOption.NOFOLLOW_LINKS)){
            Files.copy(uploadedInputStream, BASE_DIR2.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(BASE_DIR2.resolve(fileName),FINAL_DIR.resolve(fileName),StandardCopyOption.REPLACE_EXISTING);
        
        }else 
            throw new WebApplicationException(Response.status(Status.BAD_REQUEST).entity("No such category exists").build());
        try{ 
                        ProcessBuilder processb = new ProcessBuilder("/bin/sh", "try.sh",
                                fileName,FINAL_DIR_STR,MAIL_DIR_STR,extensionFL,category);
                        processb.directory(new File("/home/user/srcgreg"));
                        // redirect stdout, stderr, etc
                        Process p = processb.start();
            ;} catch(Exception tftrf) 
            {
                System.out.println("mistake  \n"+tftrf);
            }
        File f=new File(MAIL_DIR_STR+fileName);
        long currentT,initialT=System.currentTimeMillis();
        while (!f.exists()){
           currentT=System.currentTimeMillis();
           if (currentT-initialT>120000){
               throw new WebApplicationException(Response.status(Status.BAD_REQUEST).entity("Exceeded the time limit. Please try later").build());
           }    
        }

         // Return a 201 Created response with the appropriate Location header.
        return fileName;
    }

        
    /*
     * Upload a JPEG or PNG file.
     */
   /* @POST
    @Consumes({"image/jpeg", "image/png"})
    @Produces("text/plain")
    public String uploadImage(InputStream in, @HeaderParam("Content-Type") String fileType, 
    @HeaderParam("Content-Length") long fileSize) throws IOException {
        String extensionFL, initialFL;
        System.out.println("hey greg, debugging again");
       
        // Make sure the file is not larger than the maximum allowed size.
        if (fileSize > 1024 * 1024 * MAX_SIZE_IN_MB) {
            throw new WebApplicationException(Response.status(Status.BAD_REQUEST).entity("Image is larger than " + MAX_SIZE_IN_MB + "MB").build());
        }
        
        // Generate a random file name based on the current time.
        // This probably isn't 100% safe but works fine for this example.
        fileName = "" + System.currentTimeMillis();
        initialFL=fileName;
        if (fileType.equals("image/jpeg")) {
            fileName += ".jpg";extensionFL="jpg";
        } else {
            System.out.println("png");
            fileName += ".png";extensionFL="png";
        }
        
        // Copy the file to its location.
        Files.copy(in, BASE_DIR.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
        
        Files.copy(BASE_DIR.resolve(fileName),FINAL_DIR.resolve(fileName),StandardCopyOption.REPLACE_EXISTING);
        //Files.deleteIfExists(BASE_DIR.resolve(fileName)); //for safety reasons
        try{ //Runtime.getRuntime().exec("cd /home/user && ./try.sh 000061_greg.jpg /home/user/"
                           // + "Pictures/FinalDest/ /home/user/Pictures/SavedImages/mailBack/");
                        ProcessBuilder pb = new ProcessBuilder("/bin/sh", "try.sh",
                                fileName,FINAL_DIR_STR,MAIL_DIR_STR,extensionFL);
                        pb.directory(new File("/home/user"));
                        // redirect stdout, stderr, etc
                        Process p = pb.start();
                        System.out.println(initialFL);
            ;} catch(Exception tftrf) 
            {
                System.out.println("mistake "+tftrf);
            }


         // Return a 201 Created response with the appropriate Location header.
        //return Response.status(201).location(URI.create("/" + fileName)).build();    
        return fileName;
                    
    }
    
   
/* GUIDES 
 greg, August 2013
 * 
 * for uploading images (this): http://asipofjava.blogspot.gr/2013/04/upload-and-download-images-via-jax-rs.html
 * for XML parser: http://viralpatel.net/blogs/java-xml-xpath-tutorial-parse-xml/ 
 * 
 * 
 * 
 * 
 * client side: 
 * 1) see the progress of uploading: http://www.matlus.com/html5-file-upload-with-progress/
 */

/*
 * SELF NOTES: 
 * 
 * 
 * 1) When you make a request, then every new request will be a new instantiation of this class
 * Eg. when sending two requests (one with image and one with xml) the 2nd, does not know
 * anything about the image. 
 * 
 * 2) MULTIPART/FORM-DATA: i) javascript part: https://hacks.mozilla.org/2010/05/formdata-interface-coming-to-firefox/
 * ii) server side: (use headers from this): http://www.javaroots.com/2013/05/createfileuploadmulejerseyrest.html
 * 
 */
