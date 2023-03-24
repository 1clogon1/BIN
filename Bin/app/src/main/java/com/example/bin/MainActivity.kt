package com.example.bin

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.bin.adapter.MyAdapter
import com.example.bin.databinding.ActivityMainBinding
import com.example.bin.model.ListItem

import com.example.dbsqlite_v1.db.MyDBManager
import com.example.dbsqlite_v1.db.MyIntentConstans
import org.json.JSONObject

class MainActivity : AppCompatActivity(){

    private lateinit var binding: ActivityMainBinding

    private var url_click = ""
    private var phone_click = ""

    val myDBManager = MyDBManager(this)
    val myAdapter= MyAdapter(ArrayList(), this)

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //Очищаем поля
        clear()

        myDBManager.openDB()//Открываем базу данных

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        myDBManager.openDB()//Открываем базу данных
        init()


        //Нажатие на ссылку и открытие сайта
        binding.url.setOnClickListener{
            if( binding.url.text != null){
                val i = Intent(Intent.ACTION_VIEW, Uri.parse("http://"+url_click))
                startActivity(i)
            }
        }

        //Нажатие на номер и открытие телефона
        binding.phone.setOnClickListener{
            val i = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone_click"))
            startActivity(i)
        }


        //Кнопка поиска
        binding.buttonSearch.setOnClickListener{
            clickSearch()
        }


        binding.edTitle.setOnClickListener{
            myAdapter.updateAdapter(myDBManager.readDBData())
            binding.ConstrainLayoutSearch.visibility =View.VISIBLE

        }
        if(binding.edTitle.requestFocus() == true){
            myAdapter.updateAdapter(myDBManager.readDBData())
            binding.ConstrainLayoutSearch.visibility =View.VISIBLE
        }

        //Кнопка закрытия поиска
        binding.buttonCloseHistory.setOnClickListener{
            binding.ConstrainLayoutSearch.visibility =View.GONE
        }

    }

    //Функция проверки если такйо есть в списке, то удаляем и выводим в начало
    private fun isBinPovtor(dataList: ArrayList<ListItem>, text: String):Int {
        var i = 0
        var i2 = 0
        var i3 = 0
        var ii = 1000000000
        while (i < dataList.size) {
            if(dataList[i].bin == text){
                i++
                Log.d("MtLog","AAAAAAAAAAAAAAA1 $i")
                break
            }
            else{
                i++
                i2++
            }
        }
        if(dataList.size != i){
            i3 = i
        }
        else{
            i3 = 1111111100
        }

        Log.d("MtLog","AAAAAAAAAAAAAAA2 $i2")
        Log.d("MtLog","AAAAAAAAAAAAAAA2 $i3")
        return i3
    }

        //При закрытие приложения отключаемся от базы
    override fun onDestroy() {
        super.onDestroy()
        myDBManager.closeDB()
    }

    //При входе в приложение будем выводить данные
    override fun onResume() {
        super.onResume()

        //openSoftKeyboard()

        myDBManager.openDB()
        val dataList = myDBManager.readDBData()//Возвращаю базу данных(получаю данные)
        myAdapter.updateAdapter(myDBManager.readDBData())

    }
    private fun getREsult(bin: String) =with(binding){
        val url_https = "https://lookup.binlist.net/$bin"

        //Очередь в которую будут помещатся запросы
        val queue = Volley.newRequestQueue(this@MainActivity)

        //Создаем запрос типа GET
        val request = StringRequest(
            Request.Method.GET,
            url_https,
            {//Результат
                    result ->
                Log.d("MyLog", "Result: $result")

                val obj = JSONObject(result)
                //Общие
                edTitle.setText(bin.toString())
                edTitle.setText(bin)
                if(obj.has("scheme") && obj.has("scheme")!=null){
                    if(obj.getString("scheme")!="null") {
                        schemeNetwork.text = obj.getString("scheme")
                    }
                }
                if(obj.has("type") && obj.has("type")!=null){
                    if(obj.getString("type")!="null") {
                        type.text = obj.getString("type")
                    }
                }
                if(obj.has("brand") && obj.has("brand")!=null){
                    if(obj.getString("brand")!="null") {
                        brand.text = obj.getString("brand")
                    }
                }
                if(obj.has("prepaid") && obj.has("prepaid")!=null){
                    if(obj.getString("prepaid")!="null") {
                        prepaid.text = obj.getString("prepaid")
                    }
                }

                if(obj.has("prepaid") && obj.has("prepaid")!=null){
                    if(obj.getString("prepaid")!="null") {
                        if(obj.getString("prepaid") == "true"){ prepaid.text = "Yes" } else{ prepaid.text = "No" }
                    }
                }

                //number
                if (obj.has("number") && obj.has("number")!=null) {
                    if(obj.getString("number")!="null") {

                        if (obj.getJSONObject("number").has("length")) {
                            length.text = obj.getJSONObject("number").getString("length")
                        }

                        if (obj.getJSONObject("number").has("luhn")) {
                            if (obj.getJSONObject("number").getString("luhn") == "true") {
                                luhn.text = "Yes"
                            } else {
                                luhn.text = "No"
                            }

                        }
                    }
                }

                //country
                if (obj.has("country") && obj.has("country")!=null) {
                    if(obj.getString("country")!="null") {
                        if (obj.getJSONObject("country").has("name")) {
                            nameCountry.text =
                                obj.getJSONObject("country").getString("name")
                        }
                        if (obj.getJSONObject("country").has("alpha2")) {
                            alpha2.text = obj.getJSONObject("country").getString("alpha2")
                        }
                        if (obj.getJSONObject("country").has("currency")) {
                            currency.text =
                                obj.getJSONObject("country").getString("currency")
                        }
                        if (obj.getJSONObject("country")
                                .has("latitude") || obj.getJSONObject("country").has("longitude")
                        ) {
                            latitudeLongitude.text =
                                "(latitude: " + obj.getJSONObject("country")
                                    .getString("latitude") + " longitude: " + obj.getJSONObject("country")
                                    .getString("longitude") + ")"
                        }
                        if (obj.getJSONObject("country").has("numeric")) {
                            numeric.text = obj.getJSONObject("country").getString("numeric")
                        }
                    }


                }


                //bank
                if (obj.has("bank")){
                    if(obj.getString("bank")!="null"){
                        if (obj.getJSONObject("bank").has("name")) {
                            nameBank.text = obj.getJSONObject("bank").getString("name")
                        }
                        if (obj.getJSONObject("bank").has("url")) {
                            url.text = obj.getJSONObject("bank").getString("url")
                            url_click = obj.getJSONObject("bank").getString("url").toString()
                        }
                        if (obj.getJSONObject("bank").has("phone")) {
                            phone.text = obj.getJSONObject("bank").getString("phone")
                            phone_click= obj.getJSONObject("bank").getString("phone")
                        }
                        if (obj.getJSONObject("bank").has("city")) {
                            city.text = obj.getJSONObject("bank").getString("city")
                        }
                    }

                }
                else
                {
                    Log.d("fsd","noooooooo")
                }


            },
            {//Ошибка
                    error ->
                Log.d("MyLog", "Error: $error")
                val toast = Toast.makeText(applicationContext, "Ошибка сервера!!!", Toast.LENGTH_SHORT)
                toast.show()
            }
        )
        //Передаем в очередь запрос
        queue.add(request)
    }

    fun clear()=with(binding){
        edTitle.setText("")
        nameBank.text =  "---"
        schemeNetwork.text =  "---"
        type.text =  "---"
        brand.text =  "---"
        prepaid.text =  "---"
        prepaid.text =  "---"
        luhn.text = "---"
        nameCountry.text =  "---"
        alpha2.text =  "---"
        currency.text =  "---"
        latitudeLongitude.text =  "---"
        numeric.text =  "---"
        url.text =  "---"
        phone.text =  "---"
        city.text =  "---"
        length.text =  "---"


    }

    private fun clickSearch()=with(binding){
        //Log.d("dsf", "11111111111111"+edTitle.text.toString()+"   "+bin.length )
        val bin = edTitle.text.toString()

        if(!isFailEmpty()) {
            if (bin.length == 6 || bin.length == 8) {
                getREsult(bin)
                myDBManager.openDB()
                myDBManager.insertToDB(binding.edTitle.text.toString())
                val dataList = myDBManager.readDBData() //Возвращаю базу данных(получаю данные)
                clear()



//
                Log.d("MtLog", "AAAAAAAAAAAAAAA ${dataList.size}")
                val ttt = isBinPovtor(dataList, bin)
                if (ttt != 1111111100) {
                    myAdapter.removeHistoryBIN(
                        ttt - 1,
                        myDBManager
                    )//Пердали позицию свайпнутого BIN
                }


            } else {
                val toast =
                    Toast.makeText(applicationContext, "Нужно 6 или 8 цифр!!!", Toast.LENGTH_SHORT)
                toast.show()
            }
            ConstrainLayoutSearch.visibility = View.GONE
        }

    }
    //Проверка и вывод уведомления что поле путсое
    private fun isFailEmpty():Boolean{
        binding.apply {
            if(edTitle.text.isNullOrEmpty()) edTitle.error = "Вы не вписали BIN"
            return edTitle.text.isNullOrEmpty()
        }
    }


    //Функция для инициализации
    fun init()=with(binding){
        val llm = LinearLayoutManager(this@MainActivity)//Распологаем элементы по вертикали
        llm.reverseLayout = true
        recView.layoutManager = llm//Распологаем элементы по вертикали
        val swapHelper = getSwap()//Создали свайп
        swapHelper.attachToRecyclerView(recView)//Передали в созданный свайп, rcView в котором он будет работать
        recView.adapter = myAdapter//Подключаем адаптер
    }

    //Функция для удаления BIN в истории через свайп, свайп удобнее чем бы я делал зажатие потом,
    // потом окно с сообщение хотите ли вы удалить, а только после вы сможете удалить(так как это не
    // стратегическая информация тах сложностей ей не нужно), так как пользователю надоело бы удалать к
    // примеру штук 10 таким образом
    private fun getSwap(): ItemTouchHelper {
        return ItemTouchHelper(object: ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                myAdapter.removeHistoryBIN(viewHolder.adapterPosition, myDBManager)//Пердали позицию свайпнутого BIN

            }

        })
    }


}

