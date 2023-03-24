package com.example.bin.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bin.R
import com.example.bin.model.ListItem
import com.example.dbsqlite_v1.db.MyDBManager

class MyAdapter(listMain: ArrayList<ListItem>, contextMain2: Context) : RecyclerView.Adapter<MyAdapter.MyHolder>() {
    var listArray = listMain
    val context = contextMain2

    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvTitle = itemView.findViewById<TextView>(R.id.txtTitle)

        fun setData(title: String) {
            tvTitle.text = title
        }
    }

    //Функция для формирования объекта на экран(ячейки истории), то есть рисуется шаблон
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val inflater = LayoutInflater.from(parent.context)//Передали то что будем создавать(надувать)
        return MyHolder(inflater.inflate(R.layout.rc_item, parent, false))
    }

    //Функция для заполнения шаблона(передача BIN)
    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.setData(listArray.get(position).bin)//Определенный bin
    }

    override fun getItemCount(): Int {
        //Возврощаем размер массива
        return listArray.size
    }
    fun getItemList(): List<ListItem>  {
        return listArray
    }
    //Функция обновления списка, за счет перезаписи всего списка
    fun updateAdapter(listItems: List<ListItem>) {
        listArray.clear()//Очистка старого сиска
        listArray.addAll(listItems)//Добавили новый список
        notifyDataSetChanged()//Как бы сообщение для адаптера, что мы обновили список, чтобы он снова передал размер
    }

    //Функция обновления ячеек списка, за счет перезаписи всего rcView
    fun removeHistoryBIN(pos: Int, dbManager: MyDBManager) {
        dbManager.removeBINDB(listArray[pos].id.toString())//Удалем по id из базы
        listArray.removeAt(pos)//Удалаем по определенной позиции ячейку
        notifyItemRangeChanged(0, listArray.size)//Длина списка от и до listArray.size, так как длина изменилась после удалоения, поэтому мы ему передаем новые границы
        notifyItemRemoved(pos)//Передаем по какой позиции удалили
    }



}