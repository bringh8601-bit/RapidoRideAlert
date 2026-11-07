# Rapido Ride Alert Assistant

यह Android ऐप Rapido नाम वाले ऐप से आने वाले notification को पहचानकर तेज alert देता है। Alert पर टैप करने से Rapido ऐप खुलता है। Ride को Captain ऐप में स्वयं स्वीकार करना होगा।

## Build
GitHub Actions में **Build Android APK** workflow चलाएँ। सफल build के बाद workflow run के **Artifacts** भाग से `RapidoRideAlert-debug-apk` डाउनलोड करें।

## Setup
1. ऐप इंस्टॉल करें।
2. ऐप खोलकर **Notification Access** सेटिंग में इस ऐप को अनुमति दें।
3. Rapido Captain के notifications चालू रखें।
4. टेस्ट अलर्ट से आवाज/वाइब्रेशन जाँचें।

यह ऐप auto-accept, screen-click automation या Rapido के सुरक्षा नियंत्रणों को bypass नहीं करता। Notification content और package naming Rapido के संस्करण के अनुसार बदल सकते हैं; इसलिए वास्तविक ride notification पर परीक्षण आवश्यक है।
