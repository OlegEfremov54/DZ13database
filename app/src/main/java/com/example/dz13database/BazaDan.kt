package com.example.dz13database

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner

import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class BazaDan : AppCompatActivity() {
    private lateinit var toolbarBaza:Toolbar
    private lateinit var spiner_Role: Spinner
    private val db = DBHelper(this,null)

    private var role = mutableListOf(
        "Должность","Директор","Програмист","Тестировщик","Аналитик"
    )

    private lateinit var personNameET:EditText
    private lateinit var personFonET:EditText
    private lateinit var buttonAddBTN:Button
    private lateinit var buttonGetBTN:Button
    private lateinit var buttonDelBTN:Button
    private lateinit var personNameEditET:EditText
    private lateinit var personDolEditET:EditText
    private lateinit var personFonEditET:EditText

    private lateinit var spinerText:String

    @SuppressLint("Range", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_baza_dan)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //Привязываем поля
        personNameET = findViewById(R.id.personNameET)
        personFonET = findViewById(R.id.personFonET)
        buttonAddBTN = findViewById(R.id.buttonAddBTN)
        buttonGetBTN = findViewById(R.id.buttonGetBTN)
        buttonDelBTN = findViewById(R.id.buttonDelBTN)
        personNameEditET = findViewById(R.id.personNameEditET)
        personDolEditET = findViewById(R.id.personDolEditET)
        personFonEditET = findViewById(R.id.personFonEditET)

// Инициализация Тулбар
        toolbarBaza = findViewById(R.id.toolbarBaza)
        setSupportActionBar(toolbarBaza)
        title = " База данных"
        toolbarBaza.subtitle = " Вер.1.Главная страница"
        toolbarBaza.setLogo(R.drawable.bd)

//Инициализация Спинера
        spiner_Role=findViewById(R.id.spiner_Role)
        var adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            role
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spiner_Role.adapter=adapter

        // Преобразование Спинера в Текст
        val itemSelectListener: AdapterView.OnItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    viev: View?,
                    position: Int,
                    id: Long) {
                    val item = parent?.getItemAtPosition(position) as String
                    spinerText = item
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }

        spiner_Role.onItemSelectedListener = itemSelectListener

//Обработка кнопки Добавить
        buttonAddBTN.setOnClickListener{
            val name = personNameET.text.toString()
            val role = spinerText
            val telefon = personFonET.text.toString()

            db.addName(name,role,telefon)
            Toast.makeText(this, "$name, $role добавлены в базу данных",
                Toast.LENGTH_LONG).show()
            personNameET.text.clear()
            personFonET.text.clear()
            spiner_Role.setSelection(0)
        }

// Получение данных из базы
    buttonGetBTN.setOnClickListener{
        personNameEditET.text.clear()
        personDolEditET.text .clear()
        personFonEditET.text .clear()
        val cursor = db.getInfo()
        if (cursor!=null && cursor.moveToFirst()){
            cursor.moveToFirst()
            personNameEditET.append(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_NAME)) + "\n")
            personDolEditET.append(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_ROLE)) + "\n")
            personFonEditET.append(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_TELEFON)) + "\n")

        }
        while (cursor!!.moveToNext()){
            personNameEditET.append(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_NAME)) + "\n")
            personDolEditET.append(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_ROLE)) + "\n")
            personFonEditET.append(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_TELEFON)) + "\n")
        }
        cursor.close()
    }

        // Удаление БД
        buttonDelBTN.setOnClickListener {
            db.removeAll()
            personNameEditET.text.clear()
            personDolEditET.text .clear()
            personFonEditET.text .clear()
        }
    }
//Создание Меню
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.infoMenuMain -> {
                Toast.makeText(applicationContext, "Автор Ефремов О.В. Создан 28.11.2024",
                    Toast.LENGTH_LONG).show()
            }
            R.id.exitMenuMain ->{
                Toast.makeText(applicationContext, "Работа приложения завершена",
                    Toast.LENGTH_LONG).show()
                finishAffinity()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}

