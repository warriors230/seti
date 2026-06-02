# Prueba Tecnica Seti

Implementación de la prueba técnica Spring Boot para Seti.

La aplicación expone una **API REST JSON** que recibe el mensaje `enviarPedido`, transforma la solicitud al contrato **SOAP XML** `EnvioPedidosAcme` y devuelve una respuesta JSON con la estructura exacta pedida en el documento.

## Contratos implementados

### REST JSON

- `POST /api/pedidos/enviar`

#### Request

```json
{
  "enviarPedido": {
    "numPedido": "75630275",
    "cantidadPedido": "1",
    "codigoEAN": "00110000765191002104587",
    "nombreProducto": "Armario INVAL",
    "numDocumento": "1113987400",
    "direccion": "CR 72B 45 12 APT 301"
  }
}
```

#### Response

```json
{
  "enviarPedidoRespuesta": {
    "codigoEnvio": "80375472",
    "estado": "Entregado exitosamente al cliente"
  }
}
```

### SOAP XML

#### XML Request

```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                  xmlns:env="http://WSDLs/EnvioPedidos/EnvioPedidosAcme">
    <soapenv:Header/>
    <soapenv:Body>
        <env:EnvioPedidoAcme>
            <EnvioPedidoRequest>
                <pedido>75630275</pedido>
                <Cantidad>1</Cantidad>
                <EAN>00110000765191002104587</EAN>
                <Producto>Armario INVAL</Producto>
                <Cedula>1113987400</Cedula>
                <Direccion>CR 72B 45 12 APT 301</Direccion>
            </EnvioPedidoRequest>
        </env:EnvioPedidoAcme>
    </soapenv:Body>
</soapenv:Envelope>
```

#### XML Response

```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                  xmlns:env="http://WSDLs/EnvioPedidos/EnvioPedidosAcme">
    <soapenv:Header/>
    <soapenv:Body>
        <env:EnvioPedidoAcmeResponse>
            <EnvioPedidoResponse>
                <Codigo>80375472</Codigo>
                <Mensaje>Entregado exitosamente al cliente</Mensaje>
            </EnvioPedidoResponse>
        </env:EnvioPedidoAcmeResponse>
    </soapenv:Body>
</soapenv:Envelope>
```

## Ejecución local

> **Requisito:** JDK 17 instalado y configurado en el sistema.

```powershell
.\mvnw.cmd spring-boot:run
```

## Tests

```powershell
.\mvnw.cmd test
```

## Docker

Construcción y ejecución con Docker Compose:

```powershell
docker compose up --build
```

O manualmente:

```powershell
docker build -t acme-pedidos .
docker run --rm -p 8080:8080 acme-pedidos
```

Para detener el contenedor:

```powershell
docker compose down
```

## Probar con Swagger UI

Una vez levantada la aplicación (local o Docker), abrir en el navegador:

```
http://localhost:8080/swagger-ui.html
```

Pasos para probar el endpoint:

1. Hacer clic en `POST /api/pedidos/enviar` para expandirlo.
2. Hacer clic en el botón **Try it out**.
3. En el campo **Request body** reemplazar el contenido con el JSON de ejemplo:

```json
{
  "enviarPedido": {
    "numPedido": "75630275",
    "cantidadPedido": "1",
    "codigoEAN": "00110000765191002104587",
    "nombreProducto": "Armario INVAL",
    "numDocumento": "1113987400",
    "direccion": "CR 72B 45 12 APT 301"
  }
}
```

4. Hacer clic en **Execute**.
5. La respuesta esperada con código `200` es:

```json
{
  "enviarPedidoRespuesta": {
    "codigoEnvio": "80375472",
    "estado": "Entregado exitosamente al cliente"
  }
}
```
