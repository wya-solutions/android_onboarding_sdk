# 1. Quick Start
Esta guía de inicio rápido le ayudará a ejecutar un proyecto de muestra implementando el SDK para validacion de identidad. 
Para ejecutar el proyecto de ejemplo, necesitará Android Studio instalado. Para implementar el SDK en su proyecto, ir a la seccion 2 de este tutorial.

```
1. Clonar projecto de github: git clone https://github.com/wya-solutions/android_onboarding_sdk.git
2. Abrir el proyecto en Android Studio y cargar la licencia (ver punto 3).
3. Ejecutar el proyecto.
```

# 2. Implementar el SDK en su proyecto
### 2.1. Agregando repositorio maven.
En el archivo build.gradle del proyecto, agregar el siguiente repositorio:
```
allprojects {
    repositories {
        maven { url 'http://169.63.206.229:8081/repository/maven-releases/' }
        ...
        ...
    }
}
```
### 2.2. Agregando dependencias
En el archivo build.gradle de la app, agregar la siguiente dependencia:
```
dependencies {
    ...
    implementation 'com.wya.identityValidator:sdk_android_id_onboarding:1.1'
    ...
}
```
### 2.3. Compatibilidad con java 8
En el archivo build.gradle de la app, agregar lo siguiente:
```
android {
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
}
```
# 3. Licencia
### 3.1. Agregar licencia.

Cargar la licencia en el archivo strings.xml de la carpeta de recursos (/res/values/strings.xml):
```
<resources>
    ...
    <string name="wya_license_key" translatable="false">eyJhbGciOiJSUzI1NiIsInR5cCI6Ikp........</string>
    ...
</resources>

```

# 4. Ejecución del SDK
### 4.1. Llamar al validador de identidad:

```
import com.wya.identityValidator.StepperActivity;

...

Intent intent = new Intent(SignupActivity.this, StepperActivity.class);
startActivityForResult(intent, 1);

```
### 4.2. Resultado de la validación:
Si se completó el flujo correctamente, obtendremos un JWT con la informacion extraida del DNI y el resultado final de la validacion.

```
public void onActivityResult(int requestCode, int resultCode, final Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    // Check which request we're responding to
    if (requestCode == 1) {
        // Make sure the request was successful
        if (resultCode == RESULT_OK) {
            String result = data.getStringExtra("data");
            TextView text= findViewById(R.id.textView);
            try {
                JSONObject obj = new JSONObject(result);
                text.setText(obj.toString(5));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            TextView text= findViewById(R.id.textView);
            text.setText("Code: " + data.getStringExtra("code") + " - " + data.getStringExtra("message"));
        }
    }
}


JSON object obtenido de 'data.getStringExtra("data")'
{
    "data": {
        "validation": {
            "dni": {
                "success": true,
                "message": "OK"
            },
            "faceRecognition": {
                "success": true,
                "distance": 0.45
            }
        },
        "front": {
            "nro": "10000100001",
            "lastName": "DOE",
            "names": "JOHN",
            "sex": "M",
            "dni": "12345678",
            "copy": "C",
            "dateOfBirth": "07/09/1989",
            "dateOfIssue": "05/09/2017",
            "valid": true,
            "codeType": "pdf417"
        },
        "back": {
            "lastName": "DOE",
            "names": "JOHN",
            "sex": "F",
            "dni": "12345678",
            "dateOfBirth": "07/09/1989",
            "nationality": "ARG",
            "expiry": "27/08/2032",
            "valid": true,
            "checkDigit": {
                "documentNumber": {
                    "value": "6",
                    "valid": true
                },
                "dob": {
                    "value": "9",
                    "valid": true
                },
                "expiry": {
                    "value": "6",
                    "valid": true
                },
                "valid": true
            },
            "codeType": "mrz"
        },
        "jwt": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ2YWxpZGF0aW9...."
    }
}

```

### 4.3. Códigos de error:
En caso de error, se devolvera un codigo especifico en la variable 'resultCode' y dos variables en el intent: "message"(mensaje por defecto) y "code"(codigo de error).
```

resultCode: 0 - RESULT_CANCELED
- code: 001 / message: Onboarding canceled

resultCode: 1 - RESULT_NETWORK_ERROR   
- code: 100 / message: Check internet connection

resultCode: 2 - RESULT_WYA_API_ERROR
- code: 200 / message: Invalid license
- code: 201 / message: Error validating license
- code: 202 / message: Service unavailable

resultCode: 3 - RESULT_EXT_API_ERROR
- code: 300 / message: Google safetyNet - Too many requests to SafetyNet API
- code: 301 / message: Google safetyNet - Error SafetyNet API

```
