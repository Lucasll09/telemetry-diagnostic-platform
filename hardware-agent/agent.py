import pika
import json
import time
import psutil
import random

RABBITMQ_HOST = 'localhost'
QUEUE_NAME = 'telemetry.stream'
ORDER_ID = 1  # El ID de la orden que creaste en Postman

def read_cpu_temperature():
    """
    Intenta leer sensores físicos. Si Windows restringe el acceso,
    simula una prueba de estrés (benchmark) elevando la temperatura.
    """
    if hasattr(psutil, "sensors_temperatures"):
        temps = psutil.sensors_temperatures()
        if temps:
            return list(temps.values())[0][0].current
    
    # Simulación de carga térmica (entre 85°C y 97°C)
    return 85.0 + random.uniform(0, 12)

def start_telemetry():
    # 1. Configurar credenciales y conectar al Broker de RabbitMQ
    credentials = pika.PlainCredentials('admin', 'rootpassword')
    parameters = pika.ConnectionParameters(RABBITMQ_HOST, 5672, '/', credentials)
    connection = pika.BlockingConnection(parameters)
    
    channel = connection.channel()
    channel.queue_declare(queue=QUEUE_NAME, durable=True)

    print(f"[*] Agente iniciado. Monitorizando hardware para la Orden #{ORDER_ID}...")
    print("[*] Presiona CTRL+C para detener la prueba.")

    try:
        while True:
            # 2. Recopilar métricas de hardware
            cpu_temp = read_cpu_temperature()
            gpu_temp = 65.0 + random.uniform(-2, 2)
            
            # Simulamos que en muy raras ocasiones (2% de probabilidad) falla la fuente de poder
            voltage_stable = random.random() > 0.02 

            payload = {
                "orderId": ORDER_ID,
                "cpuTemperature": round(cpu_temp, 2),
                "gpuTemperature": round(gpu_temp, 2),
                "voltageStable": voltage_stable
            }

            # 3. Publicar el evento JSON en la cola
            channel.basic_publish(
                exchange='',
                routing_key=QUEUE_NAME,
                body=json.dumps(payload),
                properties=pika.BasicProperties(
                    content_type='application/json'
                )
            )

            print(f"[>] Transmitiendo -> CPU: {payload['cpuTemperature']}°C | GPU: {payload['gpuTemperature']}°C | Voltaje Estable: {payload['voltageStable']}")
            
            # Enviar telemetría cada 2 segundos
            time.sleep(2)

    except KeyboardInterrupt:
        print("\n[!] Deteniendo agente de diagnóstico...")
    finally:
        connection.close()

if __name__ == '__main__':
    start_telemetry()