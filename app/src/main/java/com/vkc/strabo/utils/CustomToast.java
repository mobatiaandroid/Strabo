/**
 *
 */
package com.vkc.strabo.utils;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.vkc.strabo.R;

/**
 * @author mobatia-user
 */
public class CustomToast {

    Activity mActivity;
    TextView mTextView;
    Toast mToast;

    public CustomToast(Activity mActivity) {
        this.mActivity = mActivity;
        init();

    }

    public void init() {
        LayoutInflater inflater = mActivity.getLayoutInflater();
        View layouttoast = inflater.inflate(R.layout.custom_toast, null);
        mTextView = (TextView) layouttoast.findViewById(R.id.texttoast);

        mToast = new Toast(mActivity);
        mToast.setView(layouttoast);
    }

    public void show(int errorCode) {
        if (errorCode == 0) {
            mTextView.setText(mActivity.getResources().getString(
                    R.string.common_error));
        }
        else if (errorCode == 1) {
            mTextView.setText("Successfully logged in.");
        }
        else if (errorCode == 2) {
            mTextView.setText("Login failed.Please try again later");
        }
        else if (errorCode == 3) {
            mTextView.setText("Successfully submitted login request");
        }
        else if (errorCode == 4) {
            mTextView.setText("Invalid user type");
        }
        else if (errorCode == 5) {
            mTextView.setText("No results found");
        }
        else if (errorCode == 6) {
            mTextView.setText("Successfully added to cart");
        }
        else if (errorCode == 7) {
            mTextView.setText("Already redeemed this gift");
        }
        else if (errorCode == 8) {
            mTextView.setText("OTP Verification Successful");
        }
        else if (errorCode == 9) {
            mTextView.setText("Incorrect OTP");
        }
        else if (errorCode == 10) {
            mTextView.setText("Please select dealers");
        }
        else if (errorCode == 11) {
            mTextView.setText("Cannot assign more than 10 Dealers");
        }
        else if (errorCode == 12) {
            mTextView.setText("Dealers added successfully");
        }
        else if (errorCode == 13) {
            mTextView.setText("No record found");
        }
        else if (errorCode == 14) {
            mTextView.setText("Please select a retailer");
        }
        else if (errorCode == 15) {
            mTextView.setText("Please enter coupon value");
        }
        else if (errorCode == 16) {
            mTextView.setText("Coupon value should not be greater than credit value");
        }
        else if (errorCode == 17) {
            mTextView.setText("Please enter coupon value to issue");
        }
        else if (errorCode == 18) {
            mTextView.setText("Coupon issued successfully");
        }
        else if (errorCode == 19) {
            mTextView.setText("Image uploaded successfully");
        }
        else if (errorCode == 20) {
            mTextView.setText("Cannot upload more than 2 images in a week");
        }
        else if (errorCode == 21) {
            mTextView.setText("Please capture an image to upload");
        }
        else if (errorCode == 22) {
            mTextView.setText("Insufficient coupon balance to redeem the gift");
        }
        else if (errorCode == 23) {
            mTextView.setText("This feature is only available for retailers");
        }
        else if (errorCode == 24) {
            mTextView.setText("Failed.Try again later");
        }
        else if (errorCode == 25) {
            mTextView.setText("Please select a distributor");
        }
        else if (errorCode == 26) {
            mTextView.setText("Profile updated successfully");
        }
        else if (errorCode == 27) {
            mTextView.setText("Profile updation failed");
        }
        else if (errorCode == 28) {
            mTextView.setText("Your registration with Strabo is on hold.Please login after verification");
        }
        else if (errorCode == 29) {
            mTextView.setText("Cannot login using multiple devices. Please contact VKC");
        }
        else if (errorCode == 30) {
            mTextView.setText("Mobile number updated successfully. Please login using new mobile number");
        }
        else if (errorCode == 31) {
            mTextView.setText("This feature is currently not available.");
        }
        else if (errorCode == 32) {
            mTextView.setText("Please select state.");
        }
        else if (errorCode == 33) {
            mTextView.setText("Please select district.");
        }
        else if (errorCode == 34) {
            mTextView.setText("OTP resend successfully.");
        }
        else if (errorCode == 35) {
            mTextView.setText("Already registered with scheme");
        }
        else if (errorCode == 36) {
            mTextView.setText("Please enter search key");
        }
        else if (errorCode == 37) {
            mTextView.setText("Please agree terms and conditions to continue.");
        }
        else if (errorCode == 38) {
            mTextView.setText("Do not have enough coupons to add to cart");
        }
        else if (errorCode == 39) {
            mTextView.setText("Add to cart failed");
        }
        else if (errorCode == 40) {
            mTextView.setText("Please enter quantity value");
        }
        else if (errorCode == 41) {
            mTextView.setText("Please select a voucher");
        }
        else if (errorCode == 42) {
            mTextView.setText("Please enter the quantity value");
        }
        else if (errorCode == 43) {
            mTextView.setText("No items in cart");
        }
        else if (errorCode == 44) {
            mTextView.setText("No dealers found");
        }
        else if (errorCode == 45) {
            mTextView.setText("Please select dealer");
        }
        else if (errorCode == 46) {
            mTextView.setText("Not enough data to place order");
        }
        else if (errorCode == 47) {
            mTextView.setText("Order Placed Successfully");
        }
        else if (errorCode == 48) {
            mTextView.setText("Unable to place order. Try again");
        }
        else if (errorCode == 49) {
            mTextView.setText("Unable to delete data. Try again");
        }
        else if (errorCode == 50) {
            mTextView.setText("No messages found");
        }
        else if (errorCode == 51) {
            mTextView.setText("No images uploaded this week");
        }
        else if (errorCode == 52) {
            mTextView.setText("Image Deleted Successfully");
        }
        else if (errorCode == 53) {
            mTextView.setText("Please enter mobile number");
        }
        else if (errorCode == 54) {
            mTextView.setText("Invalid mobile number");
        }

        else if (errorCode == 55) {
            mTextView.setText("Mobile number updated successfully. Please login again.");
        }
        else if (errorCode == 56) {
            mTextView.setText("Updation Failed.");
        }
        else if (errorCode == 57) {
            mTextView.setText("Please select user type");
        }
        else if (errorCode == 58) {
            mTextView.setText("No internet connectivity");
        }
        else if (errorCode == 59) {
            mTextView.setText("Please enter a valid quantity");
        }
        else if (errorCode == 60) {
            mTextView.setText("Quantity cannot be 0");
        }
        else if (errorCode == 61) {
            mTextView.setText("Unable to issue coupons,since there is no scheme running in your state");

        }
        else if (errorCode == 62) {
            mTextView.setText("Unable to fetch cart count,since there is no scheme running in your state");

        }
        else if (errorCode == 63) {
            mTextView.setText("Unable to update cart,since there is no scheme running in your state");

        }
        else if (errorCode == 64) {
            mTextView.setText("Unable to add to cart,since there is no scheme running in your state");

        }
        else if (errorCode == 65) {
            mTextView.setText("Please select retailer");

        }
        else if (errorCode == 66) {
            mTextView.setText("No retailer redeem data found");

        }
        else if (errorCode == 67) {
            mTextView.setText("Submission Failed");

        }

        else if(errorCode==68)
        {
            mTextView.setText("Unable to load report, since there is no scheme running in your state");
        }
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.show();
    }
    /*
     * CustomToast toast = new CustomToast(mActivity); toast.show(18);
	 */
}
