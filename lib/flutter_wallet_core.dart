import 'dart:async';

import 'package:flutter/services.dart';

class Keys {
  final String publicKey;
  final String address;

  Keys({this.publicKey, this.address});
}

class WalletCore {
  static const MethodChannel _channel =
      const MethodChannel('flutter_wallet_core');

  static Future<String> generateMnemonic() async {
    final String mnemonic =
        await _channel.invokeMethod<String>('generateMnemonic');
    return mnemonic;
  }

  static Future<Map<String, Keys>> importMnemonic(String mnemonic, String path,
      String password, List<String> symbols) async {
    final Map<String, Map<String, String>> keyInfo = await _channel
        .invokeMethod<Map<String, Map<String, String>>>('importMnemonic', {
      "mnemonic": mnemonic,
      "path": path,
      "password": password,
      "symbols": symbols.join(",")
    });

    return keyInfo.map((key, value) => MapEntry(
        key, Keys(address: value["address"], publicKey: value["publicKey"])));
  }

  static Future<String> signTx(String mnemonic, String path, String password,
      String symbol, String rawTx) async {
    final String signedTx = await _channel.invokeMethod<String>("signTx", {
      "mnemonic": mnemonic,
      "path": path,
      "password": password,
      "symbol": symbol,
      "rawTx": rawTx,
    });

    return signedTx;
  }
}
