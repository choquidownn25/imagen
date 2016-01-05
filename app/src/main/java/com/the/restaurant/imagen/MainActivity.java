package com.the.restaurant.imagen;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.the.restaurant.imagen.lista.MySimpleLinkedList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class MainActivity extends Activity  {

    HttpResponse response;

    static ArrayList<Double> Sub_total_precio = new ArrayList<Double>();
    ArrayList<String> nombreArrayList = new ArrayList<String>();

    private AlertDialog.Builder imageDialogs;
    private int elemento;
    /** The order. */
    Scanner s = null;
    private int n;
    private int tot;
    int datoinsertado;
    private int cantidad;
    private int Total_price;
    JSONArray data;
    int dato;

    //Texto
    TextView ColSubtotal;
    TextView ColImgDesc;
    TextView ColPrecios;

    // Declaracion del objeto ColaCircular
    static com.the.restaurant.imagen.cola.ColaCircular.ClaseColaCircular ColaCircular
            =new com.the.restaurant.imagen.cola.ColaCircular.ClaseColaCircular();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.carview_layout);
        Total_price=0;

        TextInputLayout mascara_campo_ColPrecios = (TextInputLayout)findViewById(R.id.mascara_campo_ColPrecios);
        ColPrecios = (TextView)findViewById(R.id.ColPrecios);
        // Permission StrictMode
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        // listView1
        final ListView lstView1 = (ListView)findViewById(R.id.listView1);

        /** JSON from URL
         * [
         * {"ImageID":"1","ImageDesc":"My Sea View 1","ImagePath":"http://www.thaicreate.com/android/pic_a.png"},
         * {"ImageID":"2","ImageDesc":"My Sea View 2","ImagePath":"http://www.thaicreate.com/android/pic_b.png"},
         * {"ImageID":"3","ImageDesc":"My Sea View 3","ImagePath":"http://www.thaicreate.com/android/pic_c.png"},
         * {"ImageID":"4","ImageDesc":"My Sea View 4","ImagePath":"http://www.thaicreate.com/android/pic_d.png"}
         * ]
         */
        String url = "http://informaticaintegral.co/imagenabajar/getJSON.php";
        //String url = "http://192.168.1.124/imagenabajar/getJSONs.php";

        try {
            data = new JSONArray(getJSONUrl(url));

            final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> map;

            for(int i = 0; i < data.length(); i++){
                JSONObject c = data.getJSONObject(i);
                map = new HashMap<String, String>();
                map.put("ImageID", c.getString("ImageID"));
                map.put("ImageDesc", c.getString("ImageDesc"));
                map.put("ImagePath", c.getString("ImagePath"));
                map.put("precio", c.getString("precio"));
                MyArrList.add(map);
            }



            lstView1.setAdapter(new ImageAdapter(this,MyArrList));

            final AlertDialog.Builder imageDialog = new AlertDialog.Builder(this);
            final LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);

            imageDialogs = new AlertDialog.Builder(this);

            // OnClick
            lstView1.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v,
                                        final int position, long id) {


                    //Add the new product to the order
                    //Product selectedProduct=products.get(position);
                    View layout = inflater.inflate(R.layout.custom_fullimage_dialog,
                            (ViewGroup) findViewById(R.id.layout_root));
                    ImageView image = (ImageView) layout.findViewById(R.id.fullimage);

                    final String sMemberID = MyArrList.get(position).get("ImageID").toString();
                    final String sName = MyArrList.get(position).get("ImageDesc").toString();
                    final String sPrecio = MyArrList.get(position).get("precio").toString();


                    try {
                        image.setImageBitmap(loadBitmap(MyArrList.get(position).get("ImagePath")));
                    } catch (Exception e) {
                        // When Error
                        image.setImageResource(android.R.drawable.ic_menu_report_image);
                    }

                    imageDialog.setIcon(android.R.drawable.btn_star_big_on);
                    imageDialog.setTitle("View : " + MyArrList.get(position).get("ImageDesc"));
                    imageDialog.setMessage("ImageID : " + sMemberID + "\n"
                                    + "ImageDesc : " + sName + "\n" + "Precio : " + sPrecio
                    );
                    imageDialog.setView(layout);
                    imageDialog.setPositiveButton(R.string.action_agregar_orden, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            //dialog.dismiss();
                            boolean done = false;

                            View layout = inflater.inflate(R.layout.orden_fullimage_dialog,
                                    (ViewGroup) findViewById(R.id.layout_root));
                            ImageView image = (ImageView) layout.findViewById(R.id.fullimage);
                            String sMemberID = MyArrList.get(position).get("ImageID").toString();
                            String sName = MyArrList.get(position).get("ImageDesc").toString();
                            String sPrecios = MyArrList.get(position).get("precio").toString();
                            int contaprecio = Integer.parseInt(sPrecios);
                            MySimpleLinkedList sll = new MySimpleLinkedList();
                            if (!done) {
                                n = contaprecio;
                                /*OrderProduct op = new OrderProduct(selectedProduct);
                                order.products.add(op);*/

                                Total_price = contaprecio + Total_price;
                                datoinsertado = Total_price;
                                //datoinsertado = ColPrecios.getText().toString()
                                String datoCol= String.valueOf(datoinsertado);
                                //datoCol=ColPrecios.getText().toString();
                                ColPrecios.setText(datoCol);
                            }


                            elemento = n;
                            elemento = datoinsertado;
                            imageDialogs.setTitle("View : " + MyArrList.get(position).get("ImageDesc"));
                            imageDialogs.setMessage("ImageID : " + sMemberID + "\n"
                                    + "ImageDesc : " + sName + "\n" + "Precio : " + elemento);
                            imageDialogs.setView(layout);
                            //Botom Cancelar
                            imageDialogs.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    dialog.dismiss();
                                    //prueba para insertar la sumatoria

                                }
                            });
                            imageDialogs.create();
                            imageDialogs.show();
                            //Prueba para insertar la suma
                        }


                    }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();
                        }
                    });

                    imageDialog.create();
                    imageDialog.show();
                }
            });




        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    //Clasae imagen
    public class ImageAdapter extends BaseAdapter
    {
        private Context context;
        private ArrayList<HashMap<String, String>> MyArr = new ArrayList<HashMap<String, String>>();

        public ImageAdapter(Context c, ArrayList<HashMap<String, String>> list)
        {
            // TODO Auto-generated method stub
            context = c;
            MyArr = list;
        }

        public int getCount() {
            // TODO Auto-generated method stub
            return MyArr.size();
        }

        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);


            if (convertView == null) {
                convertView = inflater.inflate(R.layout.activity_column, null);
            }

            // ColImage
            ImageView imageView = (ImageView) convertView.findViewById(R.id.ColImgPath);
            imageView.getLayoutParams().height = 100;
            imageView.getLayoutParams().width = 100;
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            try
            {
                imageView.setImageBitmap(loadBitmap(MyArr.get(position).get("ImagePath")));
            } catch (Exception e) {
                // When Error
                imageView.setImageResource(android.R.drawable.ic_menu_report_image);
            }

            // ColPosition
            TextView txtPosition = (TextView) convertView.findViewById(R.id.ColImgID);
            txtPosition.setPadding(10, 0, 0, 0);
            txtPosition.setText("ID : " + MyArr.get(position).get("ImageID"));

            // ColPicname
            TextView txtPicName = (TextView) convertView.findViewById(R.id.ColImgDesc);
            txtPicName.setPadding(50, 0, 0, 0);
            txtPicName.setText("Desc : " + MyArr.get(position).get("ImageDesc"));

            //Precio
            TextView txtPrecio = (TextView) convertView.findViewById(R.id.ColPrecio);
            txtPrecio.setPadding(100, 0, 0, 0);
            txtPrecio.setText("Precio : " + MyArr.get(position).get("precio"));



            return convertView;

        }

    }

    /*** Get JSON Code from URL ***/
    public String getJSONUrl(String url) {
        StringBuilder str = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        try {
            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) { // Download OK
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    str.append(line);
                }
            } else {
                Log.e("Log", "Failed to download file..");
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str.toString();
    }


    /***** Get Image Resource from URL (Start) *****/
    private static final String TAG = "ERROR";
    private static final int IO_BUFFER_SIZE = 4 * 1024;
    public static Bitmap loadBitmap(String url) {
        Bitmap bitmap = null;
        InputStream in = null;
        BufferedOutputStream out = null;

        try {
            in = new BufferedInputStream(new URL(url).openStream(), IO_BUFFER_SIZE);

            final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
            out = new BufferedOutputStream(dataStream, IO_BUFFER_SIZE);
            copy(in, out);
            out.flush();

            final byte[] data = dataStream.toByteArray();
            BitmapFactory.Options options = new BitmapFactory.Options();
            //options.inSampleSize = 1;

            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length,options);
        } catch (IOException e) {
            Log.e(TAG, "Could not load Bitmap from: " + url);
        } finally {
            closeStream(in);
            closeStream(out);
        }

        return bitmap;
    }

    private static void closeStream(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                android.util.Log.e(TAG, "Could not close stream", e);
            }
        }
    }

    private static void copy(InputStream in, OutputStream out) throws IOException {
        byte[] b = new byte[IO_BUFFER_SIZE];
        int read;
        while ((read = in.read(b)) != -1) {
            out.write(b, 0, read);
        }
    }
    /***** Get Image Resource from URL (End) *****/


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public static void Altas(int dato) throws IOException {
        //int dato;
        //System.out.println("\n\n<<< ALTAS >>>");
        //System.out.print("Dato a insertar? ---> ");
        dato=getInt();
        ColaCircular.Insertar(dato); //Invocar el metodo Insertar del objeto ColaCircular
    }

    // Funcion para capturar una cadena desde el teclado
    public static String getString() throws IOException {
        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(isr);
        String s = br.readLine();
        return s;
    }

    // Funcion para capturar un entero desde el teclado
    public static int getInt() throws IOException {
        String s = getString();

        if(s==null){
            s="0";
        }
        String datoCadena = s;
        int dato= Integer.parseInt(datoCadena);
        return dato;
    }
}
