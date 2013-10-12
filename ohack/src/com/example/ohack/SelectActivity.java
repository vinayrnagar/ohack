package com.example.ohack;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ebay.restxo.types.BuyerDetails;
import com.ebay.restxo.types.Item;
import com.ebay.restxo.types.XORequest;
import com.ebay.restxo.types.XOResponse;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

public class SelectActivity extends Activity implements OnItemSelectedListener {

	String[] address = {"Goodwill of Silicon Valley\n1080 N 7th Street, San Jose, CA 95112", 
			"Teens for Green\n2330 Marinship Way,Suite 200A Sausalito, CA 94965",
			"Breakthrough Silicon Valley\nc/o Hoover Middle School 1635 Park Avenue San Jose, CA 95126",
			"Planet Aid, Inc.\n 6730 Santa Barbara Court, Elkridge, Maryland 21075"};
	
	String shippingAdress;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build(); 
		StrictMode.setThreadPolicy(policy);
		
		setContentView(R.layout.activity_select);
		
		Spinner spinner = (Spinner) findViewById(R.id.spinner1);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
		        R.array.org_arrays, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(this);
		
		SeekBar budgetBar = (SeekBar) findViewById(R.id.seek1);
		budgetBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
	    {
	       public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
	       {
	    	  TextView seekvalue = (TextView) findViewById(R.id.seekvalue);
	    	  seekvalue.setText("$" + progress);
	       }

	      public void onStartTrackingTouch(SeekBar seekBar) {}

	      public void onStopTrackingTouch(SeekBar seekBar) {}
	    });
		
		final EditText selectDate = (EditText) findViewById(R.id.selectDate);
		selectDate.setOnClickListener(new OnClickListener() {

	        @Override
	        public void onClick(View v) {
	            // TODO Auto-generated method stub
	            //To show current date in the datepicker
	            Calendar mcurrentDate=Calendar.getInstance();
	            int mYear = mcurrentDate.get(Calendar.YEAR);
	            int mMonth = mcurrentDate.get(Calendar.MONTH);
	            int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

	            DatePickerDialog mDatePicker=new DatePickerDialog(SelectActivity.this, new OnDateSetListener() {                  
	                public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
	                   selectDate.setText(selectedmonth + "/" + selectedday + "/" + selectedyear);
	                }
	            }, mYear, mMonth, mDay);
	            mDatePicker.setTitle("Select date");                
	            mDatePicker.show();  }
	    });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.select, menu);
		return true;
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, 
            int pos, long id) {
		//Toast.makeText(SelectActivity.this, "" + pos, Toast.LENGTH_SHORT).show();
		this.shippingAdress = this.address[pos];
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public void shop(final View view) {
        try {

        	view.setEnabled(false);
        	final ProgressDialog pd = new ProgressDialog(this);
        	
        	AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
        		
        		@Override
        		protected void onPreExecute() {
        			pd.setTitle("Shopping for you...");
        			pd.setMessage("Please wait.");
        			pd.setCancelable(false);
        			pd.setIndeterminate(true);
        			pd.show();
        		}
        			
        		@Override
        		protected Void doInBackground(Void... arg0) {
        			try {
        				TextView budgetView = (TextView) findViewById(R.id.seekvalue);
        	    		EditText donateView = (EditText) findViewById(R.id.whattodonate);
        	    		EditText qtyView = (EditText) findViewById(R.id.quantity);
        	    		EditText selectDate = (EditText) findViewById(R.id.selectDate);
        	    		
        	    		String maxpriceS = budgetView.getText().toString();
        	    		int maxprice = Integer.parseInt(maxpriceS.replace("$", ""));
        	    		int qty = Integer.parseInt(qtyView.getText().toString());
        	    		String keywords = donateView.getText().toString();
        	    		String date = selectDate.getText().toString();
        			
        	    		String urlS = "http://apilw.qa.ebay.com/ws/svc/Shopping?appid=AdminApp&version=547&callname=FindItems&siteid=0&QueryKeywords=books%20190004262938%20&%20MaxEntries=40&";
        	    		keywords = keywords.replace(" ", "%20");
        	    		urlS= urlS.replace("books", keywords);
        	        	URL url = new URL(urlS);
        	            URLConnection conn = url.openConnection();

        	            InputStream in = (InputStream) conn.getContent();
        	          
        	            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        	            DocumentBuilder docBuild = factory.newDocumentBuilder();
        	       
        	            TransformerFactory transfromerfac = TransformerFactory.newInstance();
        	            Transformer transformer = transfromerfac.newTransformer();
        	            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        	            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        	            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        	            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

        	            Document xmlDoc = docBuild.parse(in);

        	            parseXML(xmlDoc, qty, maxprice);
        			} catch (Exception e) {
        				// TODO Auto-generated catch block
        				e.printStackTrace();
        			}
        			return null;
        		}
        		
        		@Override
        		protected void onPostExecute(Void result) {
        			if (pd!=null) {
        				pd.dismiss();
        				view.setEnabled(true);
        			}
        		}
        			
        	};
        	task.execute((Void[])null);
        	

        } catch(Exception e) {
        	Log.e("EXP", e.toString());
              //System.out.println(e.toString());
        }
	}
	
	private void setProgress() {
		ProgressDialog progressBar = new ProgressDialog(this);
		progressBar.setCancelable(true);
		progressBar.setMessage("File downloading ...");
		progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progressBar.setProgress(0);
		progressBar.setMax(100);
		progressBar.show();
	}
	
	private void parseXML(Document xmlresponseDoc, int qty, int maxprice) {
		
		//setProgress();
    	
		NodeList nodeListOfItems = xmlresponseDoc.getElementsByTagName("Item");
		List<Items> listofItems = new ArrayList<Items>();
		List<Items> sortedItems = new ArrayList<Items>();
		
		for(int s = 0; s < nodeListOfItems.getLength()  ; s++)
		{    
			Items item = new Items();
			Element ItemNode = (Element) nodeListOfItems.item(s);
			ItemNode.normalize();
			
			if (ItemNode.getElementsByTagName("Title").item(0) != null) {
				item.setTitle(ItemNode.getElementsByTagName("Title").item(0).getTextContent());
			}
			
			if (ItemNode.getElementsByTagName("ItemID").item(0) != null) {
				item.setItemID(ItemNode.getElementsByTagName("ItemID").item(0).getTextContent());
			}
			 
			if (ItemNode.getElementsByTagName("EndTime").item(0) !=null) {
				item.setEndDate(ItemNode.getElementsByTagName("EndTime").item(0).getTextContent());
			}
			 
			if (ItemNode.getElementsByTagName("ConvertedCurrentPrice").item(0) !=null) {
				item.setprice(ItemNode.getElementsByTagName("ConvertedCurrentPrice").item(0).getTextContent());
			}
			   
			listofItems.add(item);
		}
		
		Collections.sort(listofItems);
		String[] titlesAndPrices = new String[qty];
		float total = 0;
		int end = 30;
		
		for(int i = 0, k = 0; i < 0 + qty; i++, k++) {
			Items item = listofItems.get(i);
			if (end > item.getTitle().length()) {
				end = item.getTitle().length();
			}
			titlesAndPrices[k] = item.getTitle().substring(0, end) + ": $" + item.getprice();
			total += Float.parseFloat(item.getprice());
			Log.i("ITEM", item.getItemID());
			sortedItems.add(item);
		}
		
		httpUrl(qty, sortedItems);
		
       	Intent intent = new Intent(this, ConfirmationActivity.class);
		intent.putExtra("QUANTITY", qty);
		intent.putExtra("SHIPPING_ADDRESS", this.shippingAdress);
		intent.putExtra("TITLE_PRICE", titlesAndPrices);
		total = (float) (Math.round(total * 100.0) / 100.0);
		intent.putExtra("TOTAL", total);
    	startActivity(intent);
	}
	
	protected void httpUrl(int qty, List<Items> sortedItems) {
		URL url;
		try {
			url = new URL("http://xoaas.stratus.qa.ebay.com/restxosvc/onecallxo");
			
			String eBayUserId = "ebaygivings_buyer";
			String eBayUserPassword = "password";
			String paypalId = "qa163-006@paypal.com";
			String paypalPassword = "11111111";
			
			String itemId = "";
			String itemString = "";

			for (int i = 0; i < qty; i++) {
				itemId =  sortedItems.get(i).getItemID();
				Log.i("ITEM2", itemId);
				itemString += "<item><itemId>"+itemId+"</itemId>" +
						"<transactionId></transactionId><variationId></variationId>" +
						"<requestedQuantity>1</requestedQuantity>" +
						"</item>";
			}

			String payload = "<?xml version='1.0' encoding='UTF-8' standalone='yes'?><xoRequest><buyerDetails><ebayUserId>"
							+eBayUserId+"</ebayUserId><ebayUserPassword>"+eBayUserPassword+
							"</ebayUserPassword><paypalUserId>"+paypalId+"</paypalUserId>" +
							"<paypalUserPassword>"+paypalPassword+"</paypalUserPassword>" +
							"</buyerDetails>" +
							"<items>" +
							itemString +
							"</items></xoRequest>";

			HttpURLConnection urlconn = (HttpURLConnection)url.openConnection();
			urlconn.setDoInput(true);
			urlconn.setDoOutput(true);

			urlconn.setRequestMethod("POST");
			urlconn.setRequestProperty("Accept", "application/xml");
			urlconn.setRequestProperty("Content-Type", "application/xml");

			OutputStream os = urlconn.getOutputStream();
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os));
			writer.write(payload);
			writer.close();
			os.close();
			String line, output = "";
			BufferedReader br = new BufferedReader(new InputStreamReader(urlconn.getInputStream()));
			
			while((line = br.readLine()) != null) {
				output += line;
			}
			br.close(); urlconn.disconnect();
			Log.i("CHECKOUT", ">> " + output);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected void checkout()
	{
		System.out.println("Starting checkout ....");
        XORequest xoRequest = new XORequest();
        BuyerDetails buyerDetails = new BuyerDetails();
        buyerDetails.setEbayUserId("ebaygivings_buyer");
        buyerDetails.setEbayUserPassword("password");
        buyerDetails.setPaypalUserId("qa163-006@paypal.com");
        buyerDetails.setPaypalUserPassword("11111111");
        xoRequest.setBuyerDetails(buyerDetails);
         
        Item oneItem = new Item();
        oneItem.setItemId("130005701582");
        oneItem.setRequestedQuantity(1);
         
        List<Item> itemList = new ArrayList<Item>();
        itemList.add(oneItem);                           
        xoRequest.setItemList(itemList);
         
        Client client = new Client(); 
        ClientConfig clientConfig = new DefaultClientConfig();
        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, true);
        client = Client.create(clientConfig);   
  
        WebResource resource = client.resource("http://xoaas.stratus.qa.ebay.com/restxosvc/onecallxo");
        resource.accept(MediaType.APPLICATION_XML);
        resource.type(MediaType.APPLICATION_XML);
        
        XOResponse xoResponse = resource.post(XOResponse.class, xoRequest);
        System.out.println("Checkout Response : " +xoResponse.getAckValue());
	}
	
	
}



