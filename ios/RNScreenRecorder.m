//
//  RNScreenRecorder.m
//  RNScreenRecorder
//
//  Created by Zili Yu on 2018/6/6.
//  Copyright © 2018年 Facebook. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "RNScreenRecorder.h"
#import "ASScreenRecorder.h"
#import <React/RCTLog.h>
#import <React/RCTEventDispatcher.h>
#import <React/UIView+React.h>

@import AVFoundation;
@import AVKit;

@interface RNScreenRecorder ()

@property (nonatomic, weak) RCTBridge *bridge;

@end

@implementation RNScreenRecorder
{
    bool _isRecording;
}

- (id)initWithBridge:(RCTBridge *)bridge
{
    if ((self = [super init])) {
        self.bridge = bridge;
    }
    
    return self;
}

- (void)startRecording:(NSDictionary *)options
                     resolve:(RCTPromiseResolveBlock)resolve
                     reject:(RCTPromiseRejectBlock)reject
{
    if(_isRecording)
    {
        return ;
    }
    dispatch_async(dispatch_get_main_queue(), ^{
        ASScreenRecorder *myRecorder = [ASScreenRecorder sharedInstance];
        if(myRecorder.isRecording)
        {
            [myRecorder stopRecordingWithCompletion:nil];
        }
        NSString *tempDirectory = NSTemporaryDirectory();
        NSString *tempPath = [tempDirectory stringByAppendingPathComponent:@"video.mp4"];
        NSLog(@"Recording is starting, and the video has been saved to: %@",tempPath);
        
        //if there is a video file already, delete it!
        [self removeVideoFileWithPath:tempPath];
        myRecorder.videoURL = [NSURL fileURLWithPath:tempPath];
        //start recording !!
        [myRecorder startRecordingWithView:self];
        RCTLogInfo(@"Recording is being started!");
        _isRecording = YES;
        _hasRecording= YES;
        resolve(@"hello");
    });
}

// method for deleting an existing video file, otherwise the recording will fail
- (void)removeVideoFileWithPath:(NSString *)path
{
    NSFileManager *fileManager = [NSFileManager defaultManager];
    
    if([fileManager fileExistsAtPath:path])
    {
        NSError *error;
        if([fileManager removeItemAtPath:path error:&error] == NO)
        {
            NSLog(@"Can not delete old recording at:%@",[error localizedDescription]);
        }
    }
}

- (void) stopRecording:(NSDictionary *)options
               resolve:(RCTPromiseResolveBlock)resolve
               reject:(RCTPromiseRejectBlock)reject
{
    if(!_isRecording)
    {
        return;
    }
    dispatch_async(dispatch_get_main_queue(), ^{
        
        ASScreenRecorder *myRecorder = [ASScreenRecorder sharedInstance];
        [myRecorder stopRecordingWithCompletion:^{
            RCTLogInfo(@"Recording stops!!");
            NSLog(@"Recording stops!");
            _isRecording = NO;
        }];
    });
    ASScreenRecorder *myRecorder = [ASScreenRecorder sharedInstance];
    NSString *url = [myRecorder.videoURL absoluteString];
    resolve(url);
    
}

/*- (void) playRecording
{
    if(_isRecording){
        NSLog(@"Can not play recording while Recorder Manager is recording !!");
        return;
    }
    if(!_hasRecording){
        NSLog(@"Can not play recording, because there is no video file!!!");
        return;
    }
    dispatch_async(dispatch_get_main_queue(), ^{
        RCTLogInfo(@"Now play recording!");
        NSLog(@"Now play recording!");
        ASScreenRecorder *myRecorder = [ASScreenRecorder sharedInstance];
        
        NSLog(@"start playing recording: %@",myRecorder.videoURL);
        AVPlayer *myAVPlayer = [AVPlayer playerWithURL:myRecorder.videoURL];
        AVPlayerViewController *controller = [[AVPlayerViewController alloc] init];
        controller.player = myAVPlayer;
        [myAVPlayer play];
        
        UIViewController *myViewController = [[(AppDelegate *)[UIApplication sharedApplication].delegate window] rootViewController];
        [myViewController presentViewController:controller animated:NO completion:nil];
        
    });
}*/

@end
