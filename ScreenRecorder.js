// @flow
import React from 'react';
import {
  findNodeHandle,
  NativeModules,
  requireNativeComponent,
} from 'react-native';

const ScreenRecorderManager = NativeModules.RNScreenRecorderManager;

export default class ScreenRecorder extends React.Component {

  _screenRecorderRef;
  _screenRecorderHandle;
  
  async startRecording(options) {
    return await ScreenRecorderManager.startRecording(options, this._screenRecorderHandle);
  }
  async stopRecording(options) {
    return await ScreenRecorderManager.stopRecording(options, this._screenRecorderHandle);
  }

  _setReference = (ref) => {
    if (ref) {
      this._screenRecorderRef = ref;
      this._screenRecorderHandle = findNodeHandle(ref);
    } else {
      this._screenRecorderRef = null;
      this._screenRecorderHandle = null;
    }
  };

  render() {
    return (
        <RNScreenRecorder
            {...this.props}
            ref={this._setReference} />
    );
  }
}

const RNScreenRecorder = requireNativeComponent('RNScreenRecorder', ScreenRecorder, {
  nativeOnly: {
    // Add props here
  },
});