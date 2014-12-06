package com.example.photosharing;

/**
 * Upload Fragment is the code for the landing page
 * It requests the in-built camera activity and gets back the location of the image clicked
 * Also contains the code for uploading the image as well as data related to it to the database
 * 
 */

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.example.photosharing.R.layout;
import com.example.photosharing.ScalingUtilities.ScalingLogic;

import android.R.string;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
 
public class UploadFragment extends Fragment {
	private File dir, destImage,imagefile;
	private String cameraFile = null;

	private static final int CAPTURE_FROM_CAMERA = 1;
    private ImageView imageView;
    Bitmap photo;
    String imgLocation;
    String img;
    EditText ImageTitle;
    LinearLayout imageLayout,titleLayout;
    String msgID;
    String imgname;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        imageView = (ImageView)rootView.findViewById(R.id.showimg);
         imageLayout=(LinearLayout)rootView.findViewById(R.id.imageLayout);
        titleLayout=(LinearLayout)rootView.findViewById(R.id.titleLayout);
        ImageTitle=(EditText)rootView.findViewById(R.id.titleimg);
         ImageView photoButton = (ImageView) rootView.findViewById(R.id.click);
        ImageView uploadButton = (ImageView) rootView.findViewById(R.id.upload);
        uploadButton.setClickable(false);
        if(photo!=null)
        {
        	imageView.setImageBitmap(photo);
        	uploadButton.setClickable(true);
        	imageLayout.setVisibility(1);
        	titleLayout.setVisibility(1);
        }
        photoButton.setOnClickListener(new View.OnClickListener() {

        	/**
        	 *  We create a new folder for our application named MyApp in the ext directory
        	 *  Then we add all the new images to this directory 
        	 */
        	
        	@Override
             public void onClick(View v) {
             	dir = new File(Environment.getExternalStorageDirectory()
                         .getAbsolutePath(), "MyApp");
             if (!dir.isDirectory())
                 dir.mkdir();
             //Imagename is decided by Date and Time (timestamped) and format jpg
             img=new Date().getTime()+"";
             destImage = new File(dir, img + ".jpg");
             cameraFile = destImage.getAbsolutePath();   
             try{
                 if(!destImage.createNewFile())
                     Log.e("check", "unable to create empty file");

             }catch(IOException ex){
                 ex.printStackTrace();
             }

             imagefile = new File(destImage.getAbsolutePath());
             Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
             i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(destImage));
             /*
              * Try with EXTRA_SIZE_LIMIT to reduce image size
              */
             startActivityForResult(i,CAPTURE_FROM_CAMERA);
             }
        });
        
        //To get IMEI number or unique ID of device/USER
        TelephonyManager mngr = (TelephonyManager)getActivity().getSystemService(Context.TELEPHONY_SERVICE); 
		  msgID = mngr.getDeviceId();
		  
        uploadButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            	//get the filw from the path, values are random considered resolution
            	imgLocation=dcdFile(imgLocation,1200,2400);
            	if(!ImageTitle.getText().toString().matches(""))
            	{
            		
            		new DoFileUpload().execute(imgLocation, msgID,ImageTitle.getText().toString());
            		Toast.makeText(getActivity().getApplicationContext(), "Image Uploaded.", 
               			   Toast.LENGTH_LONG).show();
            	
            	}
            	else
            	{
            		Toast.makeText(getActivity().getApplicationContext(), "Please enter a Valid Title", 
              			   Toast.LENGTH_LONG).show();
            	}
            }
        });
        setRetainInstance(true);
        return rootView;
    }
    /*

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.imageView = (ImageView)this.findViewById(R.id.imageView1);
        Button photoButton = (Button) this.findViewById(R.id.button1);
        photoButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE); 
                startActivityForResult(cameraIntent, CAMERA_REQUEST); 
            }
        });
    }*/

    public void onActivityResult(int requestCode, int resultCode, Intent data) {  
    	super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) 
        {
          case CAPTURE_FROM_CAMERA:
            if(resultCode==getActivity().RESULT_OK)
            {
                //If camera activity sends a RESULT_OK then we can use that file
            	if(imagefile==null){
                    if(cameraFile!=null)
                        imagefile = new File(cameraFile);
                    else
                        Log.e("check", "camera file object null line no 279");
            }
              
             else
                Log.e("check", imagefile.getAbsolutePath());
                imgLocation=imagefile.getAbsolutePath();
                photo = BitmapFactory.decodeFile(imagefile.getAbsolutePath());
                imageView.setImageBitmap(photo);
                imageLayout.setVisibility(1);
                titleLayout.setVisibility(1);
                                // now use this bitmap wherever you want
            }
            break;
         }  
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
    	//Get URI of image
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


public String getRealPathFromURI(Context context, Uri contentUri) {
  //Get the actual path of the image with the name of the image
  Cursor cursor = null;
  try 
  { 
    String[] proj = { MediaStore.Images.Media.DATA };
    cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
    cursor.moveToFirst();
    return cursor.getString(column_index);
  } finally {
    if (cursor != null) {
      cursor.close();
    }
  }
}
private String dcdFile(String path,int DESIREDWIDTH, int DESIREDHEIGHT) {
    String strMyImagePath = null;
    Bitmap scaledBitmap = null;
    // Get file
    try {
        // Part 1: Decode image
        Bitmap unscaledBitmap = ScalingUtilities.decodeFile(path, DESIREDWIDTH, DESIREDHEIGHT, ScalingLogic.FIT);
        if(unscaledBitmap.getWidth()>unscaledBitmap.getHeight())
        {
        		DESIREDWIDTH=2100;
        		DESIREDHEIGHT=1600;
        }
        //in case no resolution is provided we take the above resolution
            scaledBitmap = ScalingUtilities.createScaledBitmap(unscaledBitmap, DESIREDWIDTH, DESIREDHEIGHT, ScalingLogic.FIT);
        // Store to tmp file

        String extr = Environment.getExternalStorageDirectory().toString();
        File mFolder = new File(extr + "/MyApp");
        if (!mFolder.exists()) {
            mFolder.mkdir();
        }
        String s = img+".jpg";
        
		File tempfile = new File(mFolder.getAbsolutePath(), s);

        strMyImagePath = tempfile.getAbsolutePath();
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(tempfile);
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 75, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {

            e.printStackTrace();
        } catch (Exception e) {

            e.printStackTrace();
        }

        scaledBitmap.recycle();
    } catch (Throwable e) {
    }

    if (strMyImagePath == null) {
        return path;
    }
    return strMyImagePath;

}
class DoFileUpload extends AsyncTask<String, Integer, Long>{

	final String MY_TAG = "DoFileUpload";
	
	protected Long doInBackground(String... strings ){
		HttpURLConnection connection = null;
		DataOutputStream outputStream = null;
		@SuppressWarnings("unused")
		DataInputStream inputStream = null;

		String pathToOurFile = strings[0];
		String urlServer = getResources().getString(R.string.Server_link)+"fileupload.php";
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary =  "*****";

		Log.d(MY_TAG, "upload to " + urlServer + " for file " + strings[0]);

		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 1*1024*1024;
		String ph_ID = strings[1];
		String title = strings[2];
		try
		{
			FileInputStream fileInputStream = new FileInputStream(new File(pathToOurFile) );

			URL url = new URL(urlServer);
			connection = (HttpURLConnection) url.openConnection();
			
			// Allow Inputs & Outputs
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			connection.setChunkedStreamingMode(1024 * 1024);

			// Enable POST method
			connection.setRequestMethod("POST");

			connection.setRequestProperty("Connection", "Keep-Alive");
			connection.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);
			
			outputStream = new DataOutputStream( connection.getOutputStream() );
			outputStream.writeBytes(twoHyphens + boundary + lineEnd);
			//Set the UserID
			outputStream.writeBytes("Content-Disposition: form-data; name=\"userid\"; "+ lineEnd + lineEnd + ph_ID );
			//Set the title for the image
			outputStream.writeBytes(twoHyphens + boundary + lineEnd);
			outputStream.writeBytes("Content-Disposition: form-data; name=\"title\"; "+ lineEnd + lineEnd + title);
			//Set the file to upload
			outputStream.writeBytes(twoHyphens + boundary + lineEnd);
			outputStream.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";  filename=\"" + pathToOurFile +"\"" );
			
			bytesAvailable = fileInputStream.available();
			bufferSize = Math.min(bytesAvailable, maxBufferSize);
			buffer = new byte[bufferSize];

			// Read file
			bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			int bytesWritten=0;

			while (bytesRead > 0)
			{
				outputStream.write(buffer, 0, bufferSize);
				bytesWritten+=bufferSize;
				outputStream.flush();
				bytesAvailable = fileInputStream.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			}

			outputStream.writeBytes(lineEnd);
			outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
			fileInputStream.close();
			outputStream.flush();
			outputStream.close();
			
			

			// Responses from the server (code and message)
			int respCode = connection.getResponseCode();
			String respMsg = connection.getResponseMessage();
			Log.d(MY_TAG, pathToOurFile + " success in  uploadfile.php "  + respCode + "\nMessage:" +respMsg);
		} catch (Exception ex)
		{
			Log.e(MY_TAG,pathToOurFile +"failure in uploadfile.php :"+ex.getMessage());
		} finally {
			File file = new File(pathToOurFile);
			if( file.delete())
			{
				Log.d(MY_TAG, "file delete succesfull " + file.getName());
			}

		}
		Log.e(MY_TAG,"\nIMEI :"+ph_ID);
		return null;
	}
}



}

