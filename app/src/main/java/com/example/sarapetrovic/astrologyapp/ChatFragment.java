package com.example.sarapetrovic.astrologyapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by sarapetrovic on 7/7/19.
 */

public class ChatFragment extends Fragment {

    TextView tvSign, tvGoodTraits, tvBadTraits, tvSignDate, tvFamousPeople;
    String sign = "";
    int month;
    int day;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        tvSign = view.findViewById(R.id.tvSign);
        tvGoodTraits = view.findViewById(R.id.tvGoodTraits);
        tvBadTraits = view.findViewById(R.id.tvBadTraits);
        tvSignDate = view.findViewById(R.id.tvSignDate);
        tvFamousPeople = view.findViewById(R.id.tvFamousPeople);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE);
        day = Integer.parseInt(sharedPreferences.getString("dob", "not found").split("/")[0].trim());
        month = Integer.parseInt(sharedPreferences.getString("dob", "not found").split("/")[1].trim());

        sign = getSign(day, month);
        tvSign.setText(sign);


        Astrology astrology = new Astrology();
        astrology.execute("");

        return view;
    }

    public String getSign(int day, int month){
        if (month == 12){

            if (day < 22)
                return  "Sagittarius";
            else
                return "capricorn";
        }

        else if (month == 1){
            if (day < 20)
                return  "Capricorn";
            else
                return  "aquarius";
        }

        else if (month == 2){
            if (day < 19)
                return  "Aquarius";
            else
                return "pisces";
        }

        else if(month == 3){
            if (day < 21)
                return "Pisces";
            else
                return "aries";
        }
        else if (month == 4){
            if (day < 20)
                return  "Aries";
            else
                return "taurus";
        }

        else if (month == 5){
            if (day < 21)
                return  "Taurus";
            else
                return  "gemini";
        }

        else if( month == 6){
            if (day < 21)
                return  "Gemini";
            else
               return "cancer";
        }

        else if (month == 7){
            if (day < 23)
                 return "Cancer";
            else
                return  "leo";
        }

        else if( month == 8){
            if (day < 23)
               return "Leo";
            else
                return "virgo";
        }

        else if (month == 9){
            if (day < 23)
                return "Virgo";
            else
                return "libra";
        }

        else if (month == 10){
            if (day < 23)
                return "Libra";
            else
                return "scorpio";
        }

        else if (month == 11){
            if (day < 22)
                return "scorpio";
            else
                return "sagittarius";
        }
        return "";
    }

    public class Astrology extends AsyncTask<String, Integer, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... strings) {
            JSONObject json;
            JSONArray jsonArray;

            URL url;
            try {
                url = new URL("https://zodiacal.herokuapp.com/" + sign);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                String response = "";

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    String line;
                    while((line=bufferedReader.readLine())!=null){
                        response += line;
                    }
                    Log.d("JSONRESPONSE",response);
                    jsonArray = new JSONArray(response);
                    JSONObject o = jsonArray.getJSONObject(0);
                    json =  o;
                    return  json;
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if(jsonObject!=null){
                try {

                    JSONArray goodTraits = new JSONArray(jsonObject.getString("good_traits"));
                    JSONArray badTraits = new JSONArray(jsonObject.getString("bad_traits"));
                    JSONArray famousPeople = new JSONArray(jsonObject.getString("famous_people"));
                    JSONArray compatibleSigns = new JSONArray(jsonObject.getString("compatibility"));
                    JSONArray dates = new JSONArray(jsonObject.getString("sun_dates"));
                    tvSignDate.setText(dates.getString(0) + " - " + dates.getString(1));
                    String goodT = "";
                    for (int i=0; i<goodTraits.length();i++){
                        if (i==0){
                            goodT += goodTraits.getString(i);
                        }else goodT += ", " + goodTraits.getString(i);
                    }
                    tvGoodTraits.setText(goodT);
                    String badT = "";
                    for (int i=0; i<badTraits.length();i++){
                        if (i==0){
                            badT += badTraits.getString(i);
                        }else badT += ", " + badTraits.getString(i);
                    }
                    tvBadTraits.setText(badT);
                    String fam = "";
                    for (int i=0; i<famousPeople.length();i++){
                        if (i==0){
                            fam += famousPeople.getString(i);
                        }else fam += ", " + famousPeople.getString(i);
                    }
                    tvFamousPeople.setText(fam);
                    ArrayList<String> compatibles = new ArrayList<>();
                    for (int i=0; i<compatibleSigns.length();i++){
                        compatibles.add(compatibleSigns.getString(i));
                    }
                    tvFamousPeople.setText(fam);

                    saveSignAndCompatibleOnes(compatibles);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Greska u pisanju!", Toast.LENGTH_SHORT).show();
                    return;
                }

            } else{
                Toast.makeText(getActivity(), "Ne postoji taj karakter!", Toast.LENGTH_SHORT).show();
            }

            super.onPostExecute(jsonObject);
        }
    }

    private void saveSignAndCompatibleOnes(ArrayList<String> compatibles) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("sharedPrefs",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("compatible1", compatibles.get(0));
        editor.putString("compatible2", compatibles.get(1));
        editor.apply();
    }
}
