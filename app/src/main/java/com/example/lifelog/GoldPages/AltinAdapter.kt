package com.example.lifelog.GoldPages

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.lifelog.R
import com.example.lifelog.database.Database

//RecyclerView de altınları listelemek için adapter sınıfı oluşturma
class AltinAdapter(private val context: Context, private var goldsList: MutableList<Golds>, private val gramGoldPrice: Double, private val listener: onAltinMiktariDegistiListener) : RecyclerView.Adapter<AltinAdapter.CardViewTasarimNesneleriniTutucu>() {
    //itemların görsel öğelerine erişim için viewHolder sınıfı

    val vt = Database(context)

    inner class CardViewTasarimNesneleriniTutucu(view: View) : RecyclerView.ViewHolder(view){

        //RecyclerViewdeki öğelerin viewleri
        val cardViewAltin: CardView
        val altinTuru: TextView
        val altinMiktar: TextView
        val altinTutari: TextView
        val altinGuncelleImageView: ImageView

        //Elemanlara layouttan erişiyoruz
        init {
            cardViewAltin = view.findViewById(R.id.cardViewAltin)
            altinTuru = view.findViewById(R.id.textViewAltinTuru)
            altinMiktar = view.findViewById(R.id.textViewAltinMiktar)
            altinTutari = view.findViewById(R.id.textViewAltinTutar)
            altinGuncelleImageView = view.findViewById(R.id.altinGuncelleImageView)
        }

    }

    //itemlerin layout oluşturulurken kullanılacak ViewHolderlarını döndürme
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewTasarimNesneleriniTutucu {
        val binding = LayoutInflater.from(context).inflate(R.layout.card_view_altin_tasarimi, parent, false)
        return CardViewTasarimNesneleriniTutucu(binding)
    }

    //Listedeki eleman adedini döndürme
    override fun getItemCount(): Int {
        return goldsList.size
    }

    //tıklanabilir cardView oluşturma
    override fun onBindViewHolder(holder: CardViewTasarimNesneleriniTutucu, position: Int) {

        val gold = goldsList[position]

        Log.e("AltinAdapter", "Gold Amount: ${gold.goldAmount}, Gram Gold Price: $gramGoldPrice")

        holder.altinTuru.text = "${gold.goldType} Altın"
        holder.altinMiktar.text = "${gold.goldAmount}"

        //her altın türü için when yapısı ile ayrı ayrı toplam tutarlarının hesaplanması
        val adet = gold.goldAmount
        val altinTutari = when(gold.goldType.lowercase()){
            "gram" -> adet * gramGoldPrice * 1.0
            "çeyrek" -> adet * gramGoldPrice * 1.75
            "yarım" -> adet * gramGoldPrice * 3.5
            "tam" -> adet * gramGoldPrice * 7.0
            "cumhuriyet" -> adet * gramGoldPrice * 7.216
            else -> {
                Log.e("AltinAdapter", "Bilinmeyen altın türü: ${gold.goldType}")
                0.0
            }

        }

        Log.e("AltinAdapter", "Altın Tutarı: $altinTutari")
        //cardViewlerde her altın türü için toplamTutarları formatlı şekilde yazılır.
        holder.altinTutari.text = "%.2f TL".format(altinTutari)

        holder.altinGuncelleImageView.setOnClickListener{

            val dialogViewAltinGuncelleme = LayoutInflater.from(context).inflate(R.layout.dialog_view_altin_guncelleme, null)
            val editTextAltinAdet = dialogViewAltinGuncelleme.findViewById<EditText>(R.id.editTextAltinAdet)
            val radioGrupIslem = dialogViewAltinGuncelleme.findViewById<RadioGroup>(R.id.radioGroupIslem)

            AlertDialog.Builder(context)
                .setTitle("${gold.goldType} Güncelle")
                .setView(dialogViewAltinGuncelleme)
                .setPositiveButton("Güncelle"){ _, _ ->
                    val adet = editTextAltinAdet.text.toString().toIntOrNull() ?: 0
                    val secilenId = radioGrupIslem.checkedRadioButtonId

                    if(secilenId == -1){
                        Toast.makeText(context, "İşlem seçimi yapınız.", Toast.LENGTH_SHORT).show()
                    }

                    if (adet > 0) {
                        when (secilenId) {
                            R.id.radioButtonEkle -> {
                                Goldsdao().addGoldByType(vt, gold.goldType, adet)
                            }
                            R.id.radioButtonSat -> {
                                Goldsdao().sellGoldByType(vt, gold.goldType, adet)
                            }
                        }
                        goldsList = Goldsdao().fetchAllGold(vt)
                        val yeniToplam = toplamAltinTutariHesaplama()
                        listener.onAltinMiktariDegisti(yeniToplam)
                        notifyDataSetChanged()


                    } else {
                        Toast.makeText(context, "Geçerli bir adet giriniz", Toast.LENGTH_SHORT).show()
                    }

                }
                .setNegativeButton("İptal", null)
                .show()

            Log.e("TIKLANILAN ALTIN", "Altın Türü: ${gold.goldType}")
        }

    }

    //Toplam altın tutarını hesaplamak için metod
    fun toplamAltinTutariHesaplama() : Double{
        var toplamTutar = 0.0
        //goldsList gezilir, tek tek altın türlerinin toplam tutarları hesaplanıp eklenir.
        for (i in 0 until goldsList.size) {
            val gold = goldsList[i]

            val adet = gold.goldAmount
            val altinTutari = when(gold.goldType.lowercase()) {
                "gram" -> adet * gramGoldPrice * 1.0
                "çeyrek" -> adet * gramGoldPrice * 1.75
                "yarım" -> adet * gramGoldPrice * 3.5
                "tam" -> adet * gramGoldPrice * 7.0
                "cumhuriyet" -> adet * gramGoldPrice * 7.216
                else -> 0.0
            }

            // Toplama ekle
            toplamTutar += altinTutari
        }
        return toplamTutar
    }

    fun updateList(newList: List<Golds>){
        goldsList.clear()
        goldsList.addAll(newList)

        val toplam = toplamAltinTutariHesaplama()
        listener.onAltinMiktariDegisti(toplam)
        notifyDataSetChanged()
    }

    interface onAltinMiktariDegistiListener {
        fun onAltinMiktariDegisti(yeniToplam: Double)
    }


}