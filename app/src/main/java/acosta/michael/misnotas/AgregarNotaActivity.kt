package acosta.michael.misnotas

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import org.w3c.dom.Text
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import java.util.jar.Manifest

class AgregarNotaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_nota)

        val bGuardar: Button = findViewById(R.id.btn_guardar)

        bGuardar.setOnClickListener {
            guardar_nota();
        }


    }

    fun guardar_nota() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED){
            //si no los tiene, los pide al usuario
            ActivityCompat.requestPermissions( this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 235)
        } else {
            guardar()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            235 -> {
                //pregunta si el usuario aceptó los permisos
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    guardar()
                } else {
                    //si no aceptó, coloca un mensaje
                    Toast.makeText(this, "Error: permisos denegados", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    public fun guardar(){
        val tit: TextView = findViewById(R.id.et_titulo)
        val cuerp: TextView = findViewById(R.id.et_contenido)

        var titulo = tit.text.toString()
        var cuerpo = cuerp.text.toString()

        if(titulo == "" || cuerpo == "") {
            Toast.makeText(this,"Error:campos vacíos", Toast.LENGTH_SHORT).show()
        }else{
            try{
                val archivo = File(ubicacion(), titulo + ".txt")
                val fos = FileOutputStream(archivo)
                fos.write(cuerpo.toByteArray())
                fos.close()
                Toast.makeText(this, "se guardó el archivo en la capeta pública", Toast.LENGTH_SHORT).show()
            } catch (e: Exception){
                Toast.makeText(this, "Error: nos se guardó el archivo", Toast.LENGTH_SHORT).show()
            }
        }
        finish()

    }

    private fun ubicacion(): String {
        val carpeta = File(getExternalFilesDir(null), "notas")
        if(!carpeta.exists()){
            carpeta.mkdir()
        }
        return carpeta.absolutePath
    }
}