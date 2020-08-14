package com.example.mbg;

import java.io.File;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;
import android.database.Cursor;


import android.provider.MediaStore;


public class AddWorkerFragment extends Fragment implements OnClickListener {
   
   private ImageView workerImage ;
   private Button btnSubmit;
   private SQLController dbController;

   Worker worker;
   
   Uri selectedImageUri;
	
   public AddWorkerFragment(){
	   //flag = 0;   
   }
   
  // private static final int SELECT_PICTURE = 1;
   //private static final int RESULT_OK = 1;
   private String selectedImagePath = "worker_dp";
   public static final String DEFAULT_IMAGE = "worker_dp";

   public static int resultCode = 10;
   private static int RESULT_LOAD_IMAGE = 1;
   
   // Controls
   private EditText wName;
   private EditText wPContact;
   private EditText wSContact;
   private EditText wAddress;
   private EditText wProfession;
   private EditText wRate;
   private RadioButton wDaily;
   private RadioButton wWeekly;
   private RadioButton wMonthly;
   public String imgPath = "";
   
   private ImageButton imgUpload;
   
   public static int flag = 0;  // 0 - Add, 1 - Update
   
   // Inflator View
   private View rootView;
   
   public void InitializeControls(View view) {
	   workerImage = (ImageView) view.findViewById(R.id.workerimage);
       workerImage.setImageResource(R.drawable.worker_dp);
       
       wName = (EditText)view.findViewById(R.id.name);
       wName.addTextChangedListener(new TextWatcher() {
           public void afterTextChanged(Editable s) {
               Validation.hasText(wName);
           }
           public void beforeTextChanged(CharSequence s, int start, int count, int after){}
           public void onTextChanged(CharSequence s, int start, int before, int count){}
       });
       
       
       wPContact = (EditText)view.findViewById(R.id.contact);
       wPContact.addTextChangedListener(new TextWatcher() {
           public void afterTextChanged(Editable s) {
               Validation.isPhoneNumber(wPContact, true);
           }
           public void beforeTextChanged(CharSequence s, int start, int count, int after){}
           public void onTextChanged(CharSequence s, int start, int before, int count){}
       });
       
       
       wSContact = (EditText)view.findViewById(R.id.scontact);
       wSContact.addTextChangedListener(new TextWatcher() {
           public void afterTextChanged(Editable s) {
               Validation.isPhoneNumber(wSContact, false);
           }
           public void beforeTextChanged(CharSequence s, int start, int count, int after){}
           public void onTextChanged(CharSequence s, int start, int before, int count){}
       });
       
       
       wAddress = (EditText)view.findViewById(R.id.address);
       wAddress.addTextChangedListener(new TextWatcher() {
           public void afterTextChanged(Editable s) {
               Validation.hasText(wAddress);
           }
           public void beforeTextChanged(CharSequence s, int start, int count, int after){}
           public void onTextChanged(CharSequence s, int start, int before, int count){}
       });
       
       wProfession = (EditText)view.findViewById(R.id.profession);
       wProfession.addTextChangedListener(new TextWatcher() {
           public void afterTextChanged(Editable s) {
               Validation.hasText(wProfession);
           }
           public void beforeTextChanged(CharSequence s, int start, int count, int after){}
           public void onTextChanged(CharSequence s, int start, int before, int count){}
       });
       
       
       wRate = (EditText)view.findViewById(R.id.rate);
       wRate.addTextChangedListener(new TextWatcher() {
           public void afterTextChanged(Editable s) {
               Validation.isDecimal(wRate, true);
           }
           public void beforeTextChanged(CharSequence s, int start, int count, int after){}
           public void onTextChanged(CharSequence s, int start, int before, int count){}
       });
       
       wDaily = (RadioButton)view.findViewById(R.id.rdaily);
       wWeekly = (RadioButton)view.findViewById(R.id.rweekly);
       wMonthly = (RadioButton)view.findViewById(R.id.rmonthly);
       
       imgUpload = (ImageButton)view.findViewById(R.id.img_upload);
       imgUpload.setOnClickListener(new View.OnClickListener() {
   	    	@Override
   	    	public void onClick(View v) {
   	    		selectImage();
   	    	}
   		});
       btnSubmit = (Button)view.findViewById(R.id.submit);
       btnSubmit.setOnClickListener(this);
   }
   
   private void makeUpdateForm(Bundle args) {
	   worker = (Worker) args.getSerializable("worker");
	   Log.d("Worker", worker.getName());
	   fillForm(worker);		
	    }
   
   private void fillForm(Worker worker) {
	   wName.setText(worker.getName());
	   wPContact.setText(worker.getPrimaryContact());
	   wSContact.setText(worker.getSecondaryContact());
	   wPContact.setEnabled(false);
	   wAddress.setText(worker.getAddress());
	   wProfession.setText(worker.getProfession());
	   wRate.setText(worker.getRate());
	   
	   String typeWorker = worker.getType();
	   if(typeWorker.equalsIgnoreCase("D") ){
		   wDaily.setChecked(true);   
	   } else if(typeWorker.equalsIgnoreCase("W") ){
		   wWeekly.setChecked(true);
	   }
	   else {
		   wMonthly.setChecked(true);
	   }
	   
   }
   
   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container,
           Bundle savedInstanceState) {
 
	   
	   
       rootView = inflater.inflate(R.layout.addworker_layout, container, false);
       InitializeControls(rootView);
       
       Bundle arguments = getArguments();
	   
	   if(arguments != null) {
		   makeUpdateForm(arguments);
		   flag = 1;
	   }
	   else {
		   flag = 0;
	   }
     /*  workerImage.setOnClickListener(new View.OnClickListener() {
    	    @Override
    	    public void onClick(View v) {
    	    	Intent intent = new Intent();
        	   	intent.setType("image/*");
        	   	intent.setAction(Intent.ACTION_GET_CONTENT);
        	   	startActivityForResult(Intent.createChooser(intent,"Select Picture"), SELECT_PICTURE);
    	    }
    	});
       */
	   
       return rootView;
   }
	
   
   public void onActivityResult(int requestCode, int resultCode, Intent data) {
	  // Toast.makeText(getActivity(), "Here 1", Toast.LENGTH_SHORT).show();
	   super.onActivityResult(requestCode, resultCode, data);

	   if (requestCode == RESULT_LOAD_IMAGE && null != data) {
		   
	        Uri selectedImage = data.getData();
	        String[] projection = { MediaStore.Images.Media.DATA };
	        Cursor cursor = getActivity().getContentResolver().query(selectedImage, projection, null, null, null);
	        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	        cursor.moveToFirst();
	        selectedImagePath = selectedImage.toString();
	        Toast.makeText(getActivity().getApplicationContext(),selectedImagePath, Toast.LENGTH_SHORT).show();
	        cursor.close();
	        
	      //  File imageFile = new File(getRealPathFromURI(selectedImage));
	       // selectedImagePath = imageFile.getAbsolutePath();
	        
	        imgUpload.setImageURI(selectedImage);
	    }
   }

	   
	   
	   
	 
	
	  
   /*public String getPath(Uri uri) {
       String[] projection = { MediaStore.Images.Media.DATA };
       Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
       int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
       cursor.moveToFirst();
       return cursor.getString(column_index);
   } */

   @Override
	public void onClick(View v) {
	   if (checkValidation () )
           submitForm();
       else
           Toast.makeText(getActivity(), "Form contains error", Toast.LENGTH_LONG).show();
	}

   	private boolean checkValidation() {
       boolean ret = true;

       if (!Validation.hasText(wName)) ret = false;
       if (!Validation.isPhoneNumber(wPContact, true)) ret = false;
       if (!Validation.isPhoneNumber(wSContact, false)) ret = false;
       if (!Validation.hasText(wAddress)) ret = false;
       if (!Validation.hasText(wProfession)) ret = false;
       if (!Validation.isDecimal(wRate,true)) ret = false;
       
       return ret;
   }
   
   	private void submitForm() {
   		final String name = wName.getText().toString();
		final String contact = wPContact.getText().toString();
		final String scontact = wSContact.getText().toString();
		final String address = wAddress.getText().toString();
		final String profession = wProfession.getText().toString();
		final String rate = wRate.getText().toString();
		String type = "U";
		
		if(wDaily.isChecked())
			type = "D";
		else if(wWeekly.isChecked())
			type= "W";
		else 
			type ="M";
		
		Worker currentWorker  = null;
		dbController = new SQLController(getActivity());
		dbController.open();
		if(flag == 0)
		{
			currentWorker = new Worker(name,selectedImagePath,contact,scontact,address,profession,rate,type);
			long rowId = dbController.insertWorker(currentWorker);//(name, desc);
			dbController.close();
			if(rowId == -1) {
				Toast.makeText(getActivity(), "DB Error Occured in Insertion" , Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(getActivity(), "Worker Added Successfully " , Toast.LENGTH_SHORT).show();
			}

		}
		else {
			currentWorker = new Worker(worker.getId(),name,selectedImagePath,contact,scontact,address,profession,rate,type);
			long rowId = dbController.updateWorker(currentWorker);
			dbController.close();
			if(rowId == -1) {
				Toast.makeText(getActivity(), "DB Error Occured in Insertion" , Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(getActivity(), "Worker Updated Successfully " , Toast.LENGTH_SHORT).show();
			}
		}
		
		FragmentManager fm = getFragmentManager();
		fm.beginTransaction()
        .replace(R.id.content_frame, new WorkerFragment()).commit();
			
		
   	}
   	
    private void selectImage() {
    	 
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
 
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo"))
                {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 1);
                }
                else if (options[item].equals("Choose from Gallery"))
                {
                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                    Toast.makeText(getActivity(), "Here", Toast.LENGTH_SHORT).show();
                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
}
