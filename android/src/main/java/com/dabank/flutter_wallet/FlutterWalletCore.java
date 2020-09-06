package com.dabank.flutter_wallet;

import java.util.HashMap;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel.Result;
import wallet.Wallet;
import wallet.WalletOptions;
import wallet.Wallet_;

public class FlutterWalletCore {
    static String[] allFunc = new String[]{
            "generateMnemonic",
            "importMnemonic",
            "signTx"};

    static public void callFunc(MethodCall call, Result result) {
        switch (call.method) {
            case "generateMnemonic":
                generateMnemonic(call, result);
                break;
            case "importMnemonic":
                importMnemonic(call, result);
                break;
            case "signTx":
                signTx(call, result);
                break;
            default:
                result.notImplemented();
        }
    }

    static private void generateMnemonic(MethodCall call, Result result) {
        try {
            String mnemonic = Wallet.newMnemonic();
            result.success(mnemonic);
        } catch (Exception e) {
            result.error("PROCESS_ERROR", "Unknown error when generateMnemonic", null);
        }
    }

    static private void importMnemonic(MethodCall call, Result result) {
        try {
            String mnemonic = call.argument("mnemonic");
            String path = call.argument("path");
            String password = call.argument("password");
            String symbolString = call.argument("symbols");
            boolean beta = call.argument("beta");
            boolean shareAccountWithParentChain = call.argument("shareAccountWithParentChain");

            try {
                Wallet.validateMnemonic(mnemonic);
            } catch (Exception e) {
                result.error("PARAMETER_ERROR", "Mnemonic is invalid", null);
                return;
            }
            Wallet_ wallet;
            try {
                wallet = getWalletInstance(mnemonic, path, password, beta, shareAccountWithParentChain);
            } catch (Exception e) {
                result.error("PROCESS_ERROR", "Unknown error when importing mnemonic", null);
                return;
            }
            String[] symbols = symbolString.split(",", 0);
            HashMap<String, HashMap<String, String>> keyInfo = new HashMap<String, HashMap<String, String>>();
            for (String symbol : symbols) {
                HashMap<String, String> keys = new HashMap<String, String>();
                keys.put("publicKey", wallet.derivePublicKey(symbol));
                keys.put("address", wallet.deriveAddress(symbol));
                keyInfo.put(symbol, keys);
            }
            result.success(keyInfo);
        } catch (Exception e) {
            result.error("PROCESS_ERROR", "Unknown error when generateMnemonic", null);
        }
    }


    static private void signTx(MethodCall call, Result result) {
        try {
            String mnemonic = call.argument("mnemonic");
            String rawTx = call.argument("rawTx");
            String path = call.argument("path");
            String password = call.argument("password");
            String symbol = call.argument("symbol");
            boolean beta = call.argument("beta");
            boolean shareAccountWithParentChain = call.argument("shareAccountWithParentChain");
            try {
                Wallet.validateMnemonic(mnemonic);
            } catch (Exception e) {
                result.error("PARAMETER_ERROR", "Mnemonic is invalid", null);
                return;
            }
            Wallet_ wallet = getWalletInstance(mnemonic, path, password, beta, shareAccountWithParentChain);
            String signTx = wallet.sign(symbol, rawTx);
            result.success(signTx);
        } catch (Exception e) {
            result.error("PROCESS_ERROR", "Unknown error when generateMnemonic", null);
        }
    }

    private String signTx(String mnemonic, String path, String password, String symbol, String rawTx, boolean beta, boolean shareAccountWithParentChain) {
        Wallet_ wallet = this.getWalletInstance(mnemonic, path, password, beta, shareAccountWithParentChain);
        try {
            return wallet.sign(symbol, rawTx);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * @params password: salt
     */
    static private Wallet_ getWalletInstance(
            String mnemonic, String path,
            String password, boolean beta,
            boolean shareAccountWithParentChain
    ) {
        // 以下为兼容 imtoken 的模式
        WalletOptions options = new WalletOptions();
        options.add(Wallet.withPathFormat(path));
        options.add(Wallet.withPassword(password));
        options.add(Wallet.withShareAccountWithParentChain(shareAccountWithParentChain));
        options.add(Wallet.withFlag(Wallet.FlagBBCUseStandardBip44ID));
        options.add(Wallet.withFlag(Wallet.FlagMKFUseBBCBip44ID));
        // 以上为兼容 imtoken 的模式

        Wallet_ wallet = null;
        try {
            wallet = Wallet.buildWalletFromMnemonic(mnemonic, beta, options);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return wallet;
    }

}























