package com.instirepo.app.fragments;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

import com.android.volley.Request.Method;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.VolleyError;
import com.instirepo.app.R;
import com.instirepo.app.activities.CreatePostActivity;
import com.instirepo.app.activities.HomeActivity;
import com.instirepo.app.activities.SellProductActivity;
import com.instirepo.app.application.ZApplication;
import com.instirepo.app.extras.ZUrls;
import com.instirepo.app.preferences.ZPreferences;
import com.instirepo.app.serverApi.AppRequestListener;
import com.instirepo.app.serverApi.CustomStringRequest;
import com.instirepo.app.widgets.CustomGoogleFloatingActionButton;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.MediaColumns;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class SellProductFragment2ProductDetail extends BaseFragment
		implements OnClickListener, ZUrls, AppRequestListener {

	CheckBox isWarrantyCheckBox, isBillCheckBox;
	EditText name, description, mrp, price, number, warrantyLeft;
	LinearLayout warrantyLeftContainer;
	CustomGoogleFloatingActionButton floatingActionButton;

	FrameLayout uploadImage1, uploadImage2, uploadImage3, uploadImage4;
	public static int SELECT_POST_PIC_1 = 355;
	public static int SELECT_POST_PIC_2 = 356;
	public static int SELECT_POST_PIC_3 = 357;
	public static int SELECT_POST_PIC_4 = 358;
	ImageView image1, image2, image3, image4;
	String image1send, image2send, image3send, image4send;

	ProgressDialog progressDialog;

	LinearLayout picsLayout;

	public static SellProductFragment2ProductDetail newInstance(Bundle b) {
		SellProductFragment2ProductDetail frg = new SellProductFragment2ProductDetail();
		frg.setArguments(b);
		return frg;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.sell_product_fragment_enter_details, container, false);

		name = (EditText) v.findViewById(R.id.postHeading);
		description = (EditText) v.findViewById(R.id.postdesciption);
		mrp = (EditText) v.findViewById(R.id.mrp);
		price = (EditText) v.findViewById(R.id.price);
		number = (EditText) v.findViewById(R.id.number);
		warrantyLeft = (EditText) v.findViewById(R.id.warrantyleft);
		isWarrantyCheckBox = (CheckBox) v.findViewById(R.id.warrantycheck);
		isBillCheckBox = (CheckBox) v.findViewById(R.id.billavailabe);
		warrantyLeftContainer = (LinearLayout) v.findViewById(R.id.warrantyleftcontainer);
		picsLayout = (LinearLayout) v.findViewById(R.id.linearlayoutpicsupload);
		floatingActionButton = (CustomGoogleFloatingActionButton) v.findViewById(R.id.createpostfab);

		uploadImage1 = (FrameLayout) v.findViewById(R.id.uploadimage1);
		uploadImage2 = (FrameLayout) v.findViewById(R.id.uploadimage2);
		uploadImage3 = (FrameLayout) v.findViewById(R.id.uploadimage3);
		uploadImage4 = (FrameLayout) v.findViewById(R.id.uploadimage4);

		image1 = (ImageView) v.findViewById(R.id.image1);
		image2 = (ImageView) v.findViewById(R.id.image2);
		image3 = (ImageView) v.findViewById(R.id.image3);
		image4 = (ImageView) v.findViewById(R.id.image4);

		uploadImage1.setOnClickListener(this);
		uploadImage2.setOnClickListener(this);
		uploadImage3.setOnClickListener(this);
		uploadImage4.setOnClickListener(this);

		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		int deviceWidth = getActivity().getResources().getDisplayMetrics().widthPixels;
		int heightOfPic = deviceWidth / 4;
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) picsLayout.getLayoutParams();
		params.height = heightOfPic;
		picsLayout.setLayoutParams(params);

		isWarrantyCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					warrantyLeftContainer.setVisibility(View.VISIBLE);
				} else {
					warrantyLeftContainer.setVisibility(View.GONE);
				}
			}
		});

		floatingActionButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.uploadimage1:
			Intent intent = new Intent(Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			intent.setType("image/*");
			startActivityForResult(intent, SELECT_POST_PIC_1);
			break;
		case R.id.uploadimage2:
			intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			intent.setType("image/*");
			startActivityForResult(intent, SELECT_POST_PIC_2);
			break;
		case R.id.uploadimage3:
			intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			intent.setType("image/*");
			startActivityForResult(intent, SELECT_POST_PIC_3);
			break;
		case R.id.uploadimage4:
			intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			intent.setType("image/*");
			startActivityForResult(intent, SELECT_POST_PIC_4);
			break;
		case R.id.createpostfab:
			if (checkIfComplete()) {
				uploadProductOnServer();
			}
			break;

		default:
			break;
		}
	}

	private void uploadProductOnServer() {
		HashMap<String, String> p = new HashMap<>();
		p.put("user_id", ZPreferences.getUserProfileID(getActivity()));
		p.put("category_id", ((SellProductActivity) getActivity()).categoryId + "");
		p.put("name", name.getText().toString().trim());
		p.put("description", description.getText().toString().trim());
		p.put("image1", image1send);
		p.put("image2", image2send);
		p.put("image3", image3send);
		p.put("image4", image4send);
		p.put("mrp", mrp.getText().toString().trim());
		p.put("price", price.getText().toString().trim());
		p.put("contact", number.getText().toString().trim());
		p.put("warranty_left", warrantyLeft.getText().toString().trim());
		p.put("is_warranty", Boolean.toString(isWarrantyCheckBox.isChecked()));
		p.put("is_bill", Boolean.toString(isBillCheckBox.isChecked()));

		CustomStringRequest req = new CustomStringRequest(Method.POST, uploadProduct, uploadProduct, this, p);
		req.setShouldCache(false);
		req.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		ZApplication.getInstance().addToRequestQueue(req, uploadProduct);
	}

	boolean checkEmptyEdittext(EditText et) {
		if (et.getText().toString().trim().length() == 0)
			return true;
		return false;
	}

	boolean checkIfComplete() {
		if (checkEmptyEdittext(name)) {
			makeToast("Enter product name");
			return false;
		} else if (checkEmptyEdittext(description)) {
			makeToast("Enter product description");
			return false;
		} else if (checkEmptyEdittext(mrp)) {
			makeToast("Enter product MRP");
			return false;
		} else if (checkEmptyEdittext(price)) {
			makeToast("Enter product selling price");
			return false;
		} else if (Integer.valueOf(price.getText().toString()) > Integer.valueOf(mrp.getText().toString())) {
			makeToast("Selling price cannot be greater than MRP of product");
			return false;
		} else if (checkEmptyEdittext(number)) {
			makeToast("Enter your contact number so that viewers can contact you");
			return false;
		} else if (number.getText().toString().trim().length() < 8) {
			makeToast("Enter valid contact number");
			return false;
		} else if (checkEmptyEdittext(warrantyLeft) && isWarrantyCheckBox.isChecked()) {
			makeToast("Enter the warranty period left for the product");
			return false;
		} else if (image1send == null) {
			makeToast("The first image must be uploaded. It will be the product's cover image");
			return false;
		}
		return true;
	}

	public String getStringImage(Bitmap bmp) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		byte[] imageBytes = baos.toByteArray();
		String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
		return encodedImage;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == getActivity().RESULT_OK) {
			if (requestCode == SELECT_POST_PIC_1) {
				Bitmap bm = getBitmapFromIntent(data);
				image1send = getStringImage(bm);
				image1.setImageBitmap(bm);
			} else if (requestCode == SELECT_POST_PIC_2) {
				Bitmap bm = getBitmapFromIntent(data);
				image2send = getStringImage(bm);
				image2.setImageBitmap(bm);
			} else if (requestCode == SELECT_POST_PIC_3) {
				Bitmap bm = getBitmapFromIntent(data);
				image3send = getStringImage(bm);
				image3.setImageBitmap(bm);
			} else if (requestCode == SELECT_POST_PIC_4) {
				Bitmap bm = getBitmapFromIntent(data);
				image4send = getStringImage(bm);
				image4.setImageBitmap(bm);
			}
		}
	}

	Bitmap getBitmapFromIntent(Intent data) {
		Uri selectedImageUri = data.getData();
		String[] projection = { MediaColumns.DATA };
		Cursor cursor = getActivity().managedQuery(selectedImageUri, projection, null, null, null);
		int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
		cursor.moveToFirst();
		String selectedImagePath = cursor.getString(column_index);
		Bitmap bm;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(selectedImagePath, options);
		final int REQUIRED_SIZE = 400;
		int scale = 1;
		while (options.outWidth / scale / 2 >= REQUIRED_SIZE && options.outHeight / scale / 2 >= REQUIRED_SIZE)
			scale *= 2;
		options.inSampleSize = scale;
		options.inJustDecodeBounds = false;
		bm = BitmapFactory.decodeFile(selectedImagePath, options);
		return bm;
	}

	@Override
	public void onRequestStarted(String requestTag) {
		if (requestTag.equals(uploadProduct)) {
			if (progressDialog != null)
				progressDialog.dismiss();

			progressDialog = ProgressDialog.show(getActivity(), "Uploading Product", "Saving Product on server", false,
					false);
		}
	}

	@Override
	public void onRequestFailed(String requestTag, VolleyError error) {
		if (requestTag.equals(uploadProduct)) {
			if (progressDialog != null)
				progressDialog.dismiss();

			makeToast("Error..Please Try again");
		}
	}

	@Override
	public void onRequestCompleted(String requestTag, String response) {
		if (requestTag.equals(uploadProduct)) {
			if (progressDialog != null) {
				progressDialog.dismiss();
			}

			makeToast("Success");
			Intent i = new Intent(getActivity(), HomeActivity.class);
			startActivity(i);
			getActivity().finish();
		}
	}

}
