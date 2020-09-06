package com.dabank.flutter_wallet;

import androidx.annotation.NonNull;

import java.util.Arrays;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/**
 * FlutterWalletCorePlugin
 */
public class FlutterWalletPlugin implements FlutterPlugin, MethodCallHandler {
  private MethodChannel channel;

  public static void registerWith(Registrar registrar) {
    final MethodChannel channel = new MethodChannel(registrar.messenger(), "flutter_wallet_core");
    channel.setMethodCallHandler(new FlutterWalletPlugin());
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    if (Arrays.asList(FlutterWalletCore.allFunc).contains(call.method)) {
      FlutterWalletCore.callFunc(call,result);
    } else if (Arrays.asList(FlutterWalletBBC.allFunc).contains(call.method))  {
       FlutterWalletBBC.callFunc(call,result);
    }  else if (Arrays.asList(FlutterWalletBTC.allFunc).contains(call.method))  {
      FlutterWalletBTC.callFunc(call,result);
    } else if (Arrays.asList(FlutterWalletETH.allFunc).contains(call.method))  {
      FlutterWalletETH.callFunc(call,result);
    }else{
      result.notImplemented();
    }
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
  }

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    channel = new MethodChannel(flutterPluginBinding.getFlutterEngine().getDartExecutor(), "flutter_wallet_core");
    channel.setMethodCallHandler(this);
  }
}
