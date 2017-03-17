package com.example.root.android_client;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.fingerprint.FingerprintManager;
import android.preference.PreferenceManager;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class ListFoodActivity extends Activity implements LoadJSONTask.Listener, AdapterView.OnItemClickListener {

    //Fingerprint
    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String DIALOG_FRAGMENT_TAG = "myFragment";
    private static final String SECRET_MESSAGE = "Very secret message";
    /** Alias for our key in the Android Key Store */
    private static final String KEY_NAME = "my_key";

    private KeyStore mKeyStore;
    private KeyGenerator mKeyGenerator;
    private Cipher mCipher;
    private SharedPreferences mSharedPreferences;


    //end fingerprint


    private ListView mListView;

    public static final String URL = "192.168.1.131:8000/productos";

    private List<HashMap<String, String>> mProductosMapList = new ArrayList<>();

    private static final String KEY_ID = "id";
    private static final String KEY_DESCRIPCION = "descripcion";
    private static final String KEY_PRECIO = "precio";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_food);

        mListView = (ListView) findViewById(R.id.list_view);
        mListView.setOnItemClickListener(this);


        new LoadJSONTask(this).execute(URL);


        //fingerprint
        try {

            mKeyStore = KeyStore.getInstance("AndroidKeyStore");
        } catch (KeyStoreException e) {
            throw new RuntimeException("Failed to get an instance of KeyStore", e);
        }
        try {
            mKeyGenerator = KeyGenerator
                    .getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException("Failed to get an instance of KeyGenerator", e);
        }
        try {
            mCipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/"
                    + KeyProperties.BLOCK_MODE_CBC + "/"
                    + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get an instance of Cipher", e);
        }
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        KeyguardManager keyguardManager = getSystemService(KeyguardManager.class);
        FingerprintManager fingerprintManager = getSystemService(FingerprintManager.class);
        Button purchaseButton = (Button) findViewById(R.id.purchase_button);
        if (!keyguardManager.isKeyguardSecure()) {
            // Show a message that the user hasn't set up a fingerprint or lock screen.
            Toast.makeText(this,
                    "Secure lock screen hasn't set up.\n"
                            + "Go to 'Settings -> Security -> Fingerprint' to set up a fingerprint",
                    Toast.LENGTH_LONG).show();
            purchaseButton.setEnabled(false);
            return;
        }

        // Now the protection level of USE_FINGERPRINT permission is normal instead of dangerous.
        // The line below prevents the false positive inspection from Android Studio
        /*
        if (!fingerprintManager.hasEnrolledFingerprints()) {
            purchaseButton.setEnabled(false);
            // This happens when no fingerprints are registered.
            Toast.makeText(this,
                    "Go to 'Settings -> Security -> Fingerprint' and register at least one fingerprint",
                    Toast.LENGTH_LONG).show();
            return;
        }*/
        createKey();
        purchaseButton.setEnabled(true);
        purchaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.confirmation_message).setVisibility(View.GONE);
                findViewById(R.id.encrypted_message).setVisibility(View.GONE);

                // Set up the crypto object for later. The object will be authenticated by use
                // of the fingerprint.
                if (initCipher()) {
                    // Show the fingerprint dialog. The user has the option to use the fingerprint
                    // with crypto, or you can fall back to using a server-side verified password.
                    FingerprintAuthenticationDialogFragment fragment
                            = new FingerprintAuthenticationDialogFragment();
                    fragment.setCryptoObject(new FingerprintManager.CryptoObject(mCipher));
                    boolean useFingerprintPreference = mSharedPreferences
                            .getBoolean(getString(R.string.use_fingerprint_to_authenticate_key),
                                    true);
                    if (useFingerprintPreference) {
                        fragment.setStage(
                                FingerprintAuthenticationDialogFragment.Stage.FINGERPRINT);

                    }
                    fragment.show(getFragmentManager(), DIALOG_FRAGMENT_TAG);
                } else {
                    // This happens if the lock screen has been disabled or or a fingerprint got
                    // enrolled. Thus show the dialog to authenticate with their password first
                    // and ask the user if they want to authenticate with fingerprints in the
                    // future
                    FingerprintAuthenticationDialogFragment fragment
                            = new FingerprintAuthenticationDialogFragment();
                    fragment.setCryptoObject(new FingerprintManager.CryptoObject(mCipher));
                    fragment.setStage(
                            FingerprintAuthenticationDialogFragment.Stage.NEW_FINGERPRINT_ENROLLED);
                    fragment.show(getFragmentManager(), DIALOG_FRAGMENT_TAG);
                }
            }
        });

    }

    public void onLoaded(List<Producto> productoList){

        for (Producto producto : productoList) {
            HashMap<String, String> map = new HashMap<>();

            map.put(KEY_DESCRIPCION, producto.getDescripcion());
            map.put(KEY_PRECIO, producto.getPrecio());

            mProductosMapList.add(map);
        }
        loadListView();
    }

    @Override
    public void onError() {

        Toast.makeText(this, "Error !", Toast.LENGTH_SHORT).show();
    }

    //not working
    //cambios para new branch!
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        CheckedTextView ctv = (CheckedTextView)view;

        if(ctv.isChecked()){
            Toast.makeText(this, "now it is unchecked", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "now it is checked", Toast.LENGTH_SHORT).show();
        }


        Toast.makeText(this, mProductosMapList.get(i).get(KEY_DESCRIPCION),Toast.LENGTH_LONG).show();
    }

    private void loadListView() {

        ListAdapter adapter = new SimpleAdapter(ListFoodActivity.this, mProductosMapList, R.layout.list_item,
                new String[] { KEY_DESCRIPCION, KEY_PRECIO },
                new int[] { R.id.descripcion,R.id.precio });

        mListView.setAdapter(adapter);

    }
/*
    public void confirmTransaction(View view) {

        Intent intent = new Intent(this, ConfirmarPedido.class);
        startActivity(intent);
        //Toast.makeText(this, "Compra realizada con exito", Toast.LENGTH_SHORT).show();
    }

*/
    //metodos de fingerprint
    private boolean initCipher() {
        try {
            mKeyStore.load(null);
            SecretKey key = (SecretKey) mKeyStore.getKey(KEY_NAME, null);
            mCipher.init(Cipher.ENCRYPT_MODE, key);
            return true;
        } catch (KeyPermanentlyInvalidatedException e) {
            return false;
        } catch (KeyStoreException | CertificateException | UnrecoverableKeyException | IOException
                | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }
    }

    public void onPurchased(boolean withFingerprint) {
        if (withFingerprint) {
            // If the user has authenticated with fingerprint, verify that using cryptography and
            // then show the confirmation message.
            tryEncrypt();
        } else {
            // Authentication happened with backup password. Just show the confirmation message.
            showConfirmation(null);
        }
    }

    // Show confirmation, if fingerprint was used show crypto information.
    private void showConfirmation(byte[] encrypted) {


        findViewById(R.id.confirmation_message).setVisibility(View.VISIBLE);
        if (encrypted != null) {
            TextView v = (TextView) findViewById(R.id.encrypted_message);
            v.setVisibility(View.VISIBLE);
            v.setText(Base64.encodeToString(encrypted, 0 /* flags */));
        }
    }
    /**
     * Tries to encrypt some data with the generated key in {@link #createKey} which is
     * only works if the user has just authenticated via fingerprint.
     */
    private void tryEncrypt() {
        try {
            byte[] encrypted = mCipher.doFinal(SECRET_MESSAGE.getBytes());
            //DESCONTAR DE LA BD
            //SUPUESTO TOTAL A PAGAR
            //Largo la nueva activity
            int total = 50;
            Intent intent4 = new Intent(ListFoodActivity.this, PaymentActivity.class);
            startActivity(intent4);

            showConfirmation(encrypted);
        } catch (BadPaddingException | IllegalBlockSizeException e) {
            Toast.makeText(this, "Falló al encriptar la huella digital con la clave generada. "
                    + "Intente nuevamente realizar la compra", Toast.LENGTH_LONG).show();
            Log.e(TAG, "Falló al encriptar la huella digital con la clave generada." + e.getMessage());
        }
    }

    /**
     * Creates a symmetric key in the Android Key Store which can only be used after the user has
     * authenticated with fingerprint.
     */
    public void createKey() {
        // The enrolling flow for fingerprint. This is where you ask the user to set up fingerprint
        // for your flow. Use of keys is necessary if you need to know if the set of
        // enrolled fingerprints has changed.
        try {
            mKeyStore.load(null);
            // Set the alias of the entry in Android KeyStore where the key will appear
            // and the constrains (purposes) in the constructor of the Builder
            mKeyGenerator.init(new KeyGenParameterSpec.Builder(KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    // Require the user to authenticate with a fingerprint to authorize every use
                    // of the key
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());
            mKeyGenerator.generateKey();
        } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException
                | CertificateException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}

