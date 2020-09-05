import 'dart:async';

import 'package:flutter/services.dart';

class WalletBTC {
  static const _channel = MethodChannel('flutter_wallet_core');

  static Future<String> createBTCTransaction(
    String inputJson,
    String toAddress,
    double toAmount,
    String fromAddress,
    double backAmount,
    String scriptPubKey,
    String redeemScript,
    double chainID,
    double feeRate,
    bool isTestNet,
  ) async {
    final params = {
      'inputJson': inputJson,
      'toAddress': toAddress,
      'toAmount': toAmount,
      'fromAddress': fromAddress,
      'backAmount': backAmount,
      'scriptPubKey': scriptPubKey,
      'redeemScript': redeemScript,
      'chainID': chainID,
      'feeRate': feeRate,
      'isTestNet': isTestNet,
    };

    final result = await _channel.invokeMethod<String>(
      'createBTCTransaction',
      params,
    );
    return result;
  }
}
