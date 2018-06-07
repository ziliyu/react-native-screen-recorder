//
//  RNScreenRecorder.h
//  RNScreenRecorder
//
//  Created by Zili Yu on 2018/6/6.
//  Copyright © 2018年 Facebook. All rights reserved.
//

#import <React/RCTView.h>
#import <React/RCTEventDispatcher.h>
#import <AVFoundation/AVFoundation.h>

@class RCTEventDispatcher;

@interface RNScreenRecorder : UIView

@property (nonatomic, assign) BOOL isRecording;
@property (nonatomic, assign) BOOL hasRecording;

- (id)initWithBridge:(RCTBridge *)bridge;

- (void)startRecording:(NSDictionary *)options
              resolve:(RCTPromiseResolveBlock)resolve
              reject:(RCTPromiseRejectBlock)reject;
-(void)stopRecording:(NSDictionary *)options
             resolve:(RCTPromiseResolveBlock)resolve
             reject:(RCTPromiseRejectBlock)reject;
@end
