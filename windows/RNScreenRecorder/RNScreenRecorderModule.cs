using ReactNative.Bridge;
using System;
using System.Collections.Generic;
using Windows.ApplicationModel.Core;
using Windows.UI.Core;

namespace Screen.Recorder.RNScreenRecorder
{
    /// <summary>
    /// A module that allows JS to share data.
    /// </summary>
    class RNScreenRecorderModule : NativeModuleBase
    {
        /// <summary>
        /// Instantiates the <see cref="RNScreenRecorderModule"/>.
        /// </summary>
        internal RNScreenRecorderModule()
        {

        }

        /// <summary>
        /// The name of the native module.
        /// </summary>
        public override string Name
        {
            get
            {
                return "RNScreenRecorder";
            }
        }
    }
}
