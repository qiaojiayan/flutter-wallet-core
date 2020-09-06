package com.dabank.flutter_wallet_core;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import bbc.Bbc;
import bbc.KeyInfo;
import bip39.Bip39;
import btc.BTCAddress;
import btc.BTCAmount;
import btc.BTCOutputAmount;
import btc.BTCTransaction;
import btc.BTCUnspent;
import btc.Btc;
import eth.BigInt;
import eth.ETHAddress;
import eth.ETHTransaction;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel.Result;
import wallet.Wallet;
import wallet.WalletOptions;
import wallet.Wallet_;

public class FlutterWalletBTC {
    static String[] allFunc = new String[]{"createBTCTransaction"};

    static public void callFunc(MethodCall call, Result result) {
        switch (call.method) {
            case "createBTCTransaction":
                createBTCTransaction(call, result);
                break;
            default:
                result.notImplemented();
        }
    }

    static private void createBTCTransaction(MethodCall call, Result result) {
        try {
            String inputJson = call.argument("inputJson");
            String toAddress = call.argument("toAddress");
            double toAmount = call.argument("toAmount");
            String fromAddress = call.argument("fromAddress");
            double backAmount = call.argument("backAmount");
            boolean isTestNet = call.argument("isTestNet");
            String scriptPubKey = call.argument("scriptPubKey");
            String redeemScript = call.argument("redeemScript");
            long chainID = call.argument("chainID");
            long feeRate = call.argument("feeRate");

            BTCUnspent unspent = new BTCUnspent();
            JSONArray array = new JSONArray(inputJson);
            for (int i = 0; i < array.length(); i++) {
                JSONObject ob = (JSONObject) array.get(i);
                unspent.add(ob.getString("txId"), ob.getLong("vOut"), toAmount, scriptPubKey, redeemScript);
            }
            BTCOutputAmount outputAmount = new BTCOutputAmount();
            outputAmount.add(new BTCAddress(toAddress, chainID), new BTCAmount(toAmount));
            outputAmount.add(new BTCAddress(fromAddress, chainID), new BTCAmount(backAmount));

            BTCAddress change = new BTCAddress(fromAddress, chainID);

            BTCTransaction trans = new BTCTransaction(unspent, outputAmount, change, feeRate, chainID);
            String data = trans.encodeToSignCmd();
            result.success(data);
        } catch (Exception e) {
            result.error("PROCESS_ERROR", "Unknown error when createBTCTransaction", null);
        }
    }

}
