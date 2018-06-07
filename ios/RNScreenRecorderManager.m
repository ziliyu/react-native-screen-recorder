
#import <UIKit/UIKit.h>
#import "ASScreenRecorder.h"
#import "AppDelegate.h"
#import <React/RCTLog.h>
#import <React/RCTUIManager.h>
#import "RNScreenRecorderManager.h"
#import "RNScreenRecorder.h"

@implementation RNScreenRecorderManager

RCT_EXPORT_MODULE()

@synthesize bridge = _bridge;

- (UIView *)view
{
    return [[RNScreenRecorder alloc] initWithBridge:self.bridge];
}

+ (BOOL)requiresMainQueueSetup
{
    return YES;
}

RCT_REMAP_METHOD(startRecording,
                 options:(NSDictionary *)options
                 reactTag:(nonnull NSNumber *)reactTag
                 resolver:(RCTPromiseResolveBlock)resolve
                 rejecter:(RCTPromiseRejectBlock)reject)
{
    [self.bridge.uiManager addUIBlock:^(__unused RCTUIManager *uiManager, NSDictionary<NSNumber *, RNScreenRecorder *> *viewRegistry) {
        RNScreenRecorder *view = viewRegistry[reactTag];
        if (![view isKindOfClass:[RNScreenRecorder class]]) {
            RCTLogError(@"Invalid view returned from registry, expecting RNScreenRecorder, got: %@", view);
        } else {
            [view startRecording:options resolve:resolve reject:reject];
        }
    }];
}

RCT_REMAP_METHOD(stopRecording,
                 withOptions:(NSDictionary *)options
                 reactTag:(nonnull NSNumber *)reactTag
                 resolver:(RCTPromiseResolveBlock)resolve
                 rejecter:(RCTPromiseRejectBlock)reject)
{
    [self.bridge.uiManager addUIBlock:^(__unused RCTUIManager *uiManager, NSDictionary<NSNumber *, RNScreenRecorder *> *viewRegistry) {
        RNScreenRecorder *view = viewRegistry[reactTag];
        if (![view isKindOfClass:[RNScreenRecorder class]]) {
            RCTLogError(@"Invalid view returned from registry, expecting RNScreenRecorder, got: %@", view);
        } else {
            [view stopRecording:options resolve:resolve reject:reject];
        }
    }];
}

@end
  
