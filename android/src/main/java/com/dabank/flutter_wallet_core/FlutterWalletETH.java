package com.dabank.flutter_wallet_core;

import org.json.JSONArray;
import org.json.JSONObject;

import btc.BTCAddress;
import btc.BTCAmount;
import btc.BTCOutputAmount;
import btc.BTCTransaction;
import btc.BTCUnspent;
import eth.BigInt;
import eth.ETHAddress;
import eth.ETHTransaction;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel.Result;

public class FlutterWalletETH {
    static String[] allFunc = new String[]{"createETHTransaction"};

    static public void callFunc(MethodCall call, Result result) {
        switch (call.method) {
            case "createETHTransaction":
                createETHTransaction(call, result);
                break;
            default:
                result.notImplemented();
        }
    }

    static private void createETHTransaction(MethodCall call, Result result) {
        try {


            double nonce = call.argument("nonce");
            int gasLimit = call.argument("gasLimit");
            String address = call.argument("address");
            double amount = call.argument("amount");
            double gasPrice = call.argument("gasPrice");
            boolean isTestNet = call.argument("isTestNet");

            ETHAddress ethAddress = new ETHAddress(address);
            BigInt bigAmount = new BigInt(new Double(amount).longValue());
            BigInt bigGasPrice = new BigInt(new Double(gasPrice).longValue());
            ETHTransaction trans = new ETHTransaction(new Double(nonce).longValue(), ethAddress, bigAmount, gasLimit, bigGasPrice, null);
            String data = trans.encodeRLP();
            result.success(data);
        } catch (Exception e) {
            result.error("PROCESS_ERROR", "Unknown error when createETHTransaction", null);
        }
    }

}
