package com.example.lifelog.KriptoPages

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lifelog.R
import com.example.lifelog.database.Crypto
import com.example.lifelog.databinding.ActivityAllCryptoBinding

class AllCryptoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAllCryptoBinding
    private lateinit var cryptoList: ArrayList<Crypto>
    private lateinit var adapter: KriptoRecView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_crypto)
        binding= ActivityAllCryptoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        cryptoList= ArrayList<Crypto>()
        val cryptoMap = hashMapOf(
            "BTC" to "Bitcoin",
            "ETH" to "Ethereum",
            "USDT" to "Tether",
            "BNB" to "Binance Coin",
            "SOL" to "Solana",
            "XRP" to "XRP",
            "USDC" to "USD Coin",
            "DOGE" to "Dogecoin",
            "ADA" to "Cardano",
            "TRX" to "TRON",
            "DOT" to "Polkadot",
            "SHIB" to "Shiba Inu",
            "AVAX" to "Avalanche",
            "MATIC" to "Polygon",
            "DAI" to "Dai",
            "WBTC" to "Wrapped Bitcoin",
            "LTC" to "Litecoin",
            "UNI" to "Uniswap",
            "LINK" to "Chainlink",
            "ATOM" to "Cosmos",
            "XMR" to "Monero",
            "ETC" to "Ethereum Classic",
            "BCH" to "Bitcoin Cash",
            "XLM" to "Stellar",
            "FIL" to "Filecoin",
            "NEAR" to "NEAR Protocol",
            "APT" to "Aptos",
            "HBAR" to "Hedera",
            "VET" to "VeChain",
            "ICP" to "Internet Computer",
            "LDO" to "Lido DAO",
            "ARB" to "Arbitrum",
            "QNT" to "Quant",
            "OP" to "Optimism",
            "GRT" to "The Graph",
            "ALGO" to "Algorand",
            "AAVE" to "Aave",
            "EGLD" to "MultiversX",
            "MKR" to "Maker",
            "SAND" to "The Sandbox",
            "XTZ" to "Tezos",
            "AXS" to "Axie Infinity",
            "THETA" to "Theta Network",
            "EOS" to "EOS",
            "IMX" to "Immutable X",
            "SNX" to "Synthetix",
            "RPL" to "Rocket Pool",
            "KAVA" to "Kava",
            "FLOW" to "Flow",
            "CHZ" to "Chiliz",
            "CRV" to "Curve DAO Token",
            "GALA" to "Gala",
            "ENJ" to "Enjin Coin",
            "ZEC" to "Zcash",
            "KSM" to "Kusama",
            "NEO" to "Neo",
            "CRO" to "Cronos",
            "BAT" to "Basic Attention Token",
            "1INCH" to "1inch",
            "DYDX" to "dYdX",
            "COMP" to "Compound",
            "UMA" to "UMA",
            "YFI" to "yearn.finance",
            "LRC" to "Loopring",
            "RSR" to "Reserve Rights",
            "BNT" to "Bancor",
            "STORJ" to "Storj",
            "ZEN" to "Horizen",
            "SKL" to "SKALE",
            "ANKR" to "Ankr",
            "OCEAN" to "Ocean Protocol",
            "REN" to "Ren",
            "BAL" to "Balancer",
            "NMR" to "Numeraire",
            "CVC" to "Civic",
            "SUSHI" to "SushiSwap",
            "FET" to "Fetch.ai",
            "BAND" to "Band Protocol",
            "DGB" to "DigiByte",
            "KNC" to "Kyber Network",
            "RUNE" to "THORChain",
            "CELR" to "Celer Network",
            "API3" to "API3",
            "LPT" to "Livepeer",
            "AR" to "Arweave",
            "XDC" to "XDC Network",
            "HNT" to "Helium",
            "ONT" to "Ontology",
            "ZIL" to "Zilliqa",
            "ICX" to "ICON",
            "VTHO" to "VeThor Token",
            "SC" to "Siacoin",
            "CKB" to "Nervos Network",
            "STMX" to "StormX",
            "WIN" to "WINkLink",
            "BTT" to "BitTorrent",
            "MTL" to "Metal DAO",
            "AMP" to "Amp",
            "XEM" to "NEM",
            "DENT" to "Dent"
        )

        for(i in cryptoMap){
            var kripto=Crypto(i.key,i.value)
            cryptoList.add(kripto)
        }
        binding.KriptoRV.setHasFixedSize(true)
        binding.KriptoRV.layoutManager= LinearLayoutManager(this)

        adapter= KriptoRecView(this,cryptoList)
        binding.KriptoRV.adapter=adapter
    }
}