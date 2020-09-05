import 'dart:async';

import 'package:flutter/services.dart';

class WalletETH {
  static const _channel = MethodChannel('flutter_wallet_core');

  static Future<String> createETHTransaction(
    double nonce,
    int gasLimit,
    String address,
    double amount,
    double gasPrice,
    bool isTestNet,
  ) async {
    final params = {
      'nonce': nonce,
      'gasLimit': gasLimit,
      'address': address,
      'amount': amount,
      'gasPrice': gasPrice,
      'isTestNet': isTestNet,
    };

    final result = await _channel.invokeMethod<String>(
      'createETHTransaction',
      params,
    );
    return result;
  }
}
