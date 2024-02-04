import pika
import json
from datetime import datetime
import time
import board
import adafruit_dht
import RPi.GPIO as GPIO
import threading


dht_device = adafruit_dht.DHT11(board.D17)

motorspeed_pin = 14
DIRA = 15
DIRB = 25
red_led_pin = 20
green_led_pin = 21

GPIO.setup(motorspeed_pin, GPIO.OUT)
GPIO.setup(DIRA, GPIO.OUT)
GPIO.setup(DIRB, GPIO.OUT)
GPIO.setup(red_led_pin, GPIO.OUT)
GPIO.setup(green_led_pin, GPIO.OUT)

pwmPIN = GPIO.PWM(motorspeed_pin, 100)
pwmPIN.start(0)

rabbitmq_url = 'amqp://iot:Masterazzi123@20.54.31.176:15672'

feedback_exchange_name = 'iot.feedback.exchange'
feedback_queue_name = 'iot.feedback.queue'
feedback_routing_key = 'iot.feedback.routingkey'

exchange_name = 'iot.exchange'
queue_name = 'iot.queue'
routing_key = 'iot.routingkey'


def control_motor(run_time, direction):
    pwmPIN.ChangeDutyCycle(100)
    if direction == 'forward':
        GPIO.output(DIRA, GPIO.HIGH)
        GPIO.output(DIRB, GPIO.LOW)
    elif direction == 'backward':
        GPIO.output(DIRA, GPIO.LOW)
        GPIO.output(DIRB, GPIO.HIGH)
    time.sleep(run_time)
    pwmPIN.ChangeDutyCycle(0)
    GPIO.output(DIRA, GPIO.LOW)
    GPIO.output(DIRB, GPIO.LOW)

def send_data_to_rabbitmq(temperature, humidity):
    data_to_send = {
        "eventName": "EventData",
        "temperature": temperature,
        "humidity": humidity
    }

    data_message = json.dumps(data_to_send)

    try:
        
        channel.basic_publish(
            exchange=exchange_name,
            routing_key=routing_key,
            body=data_message,
            properties=pika.BasicProperties(content_type='application/json')
        )
    except Exception as e:
        print(e)

    print(data_to_send)

def feedback_callback(ch, method, properties, body):
    feedback_data = json.loads(body)

    heater_switch = feedback_data.get("heaterSwitch", False)
    fan_switch = feedback_data.get("fanSwitch", False)

    if heater_switch:
        GPIO.output(red_led_pin, GPIO.HIGH)
        time.sleep(10)
        GPIO.output(red_led_pin, GPIO.LOW)

    if fan_switch:
        control_motor(10, 'forward')

    print(feedback_data)


parameters = pika.URLParameters(rabbitmq_url)
connection = pika.BlockingConnection(parameters)
channel = connection.channel()

channel.exchange_declare(exchange=feedback_exchange_name, exchange_type='topic', durable=True)
channel.queue_declare(queue=feedback_queue_name, durable=True)
channel.queue_bind(exchange=feedback_exchange_name, queue=feedback_queue_name, routing_key=feedback_routing_key)

channel.exchange_declare(exchange=exchange_name, exchange_type='topic',durable=True)
channel.queue_declare(queue=queue_name, durable=True)
channel.queue_bind(exchange=exchange_name, queue=queue_name, routing_key=routing_key)

def consume_feedback():
    while True:
        try:
            method_frame, header_frame, body = channel.basic_get(queue=feedback_queue_name, auto_ack=True)
            if method_frame:
                feedback_callback(None, None, None, body)
        except Exception as e:
            print(e)
        time.sleep(1)
        
feedback_thread = threading.Thread(target=consume_feedback)
feedback_thread.daemon = True
feedback_thread.start()

try:
    while True:
        try:
            temperature = dht_device.temperature
            humidity = dht_device.humidity

            if temperature is not None:
                send_data_to_rabbitmq(temperature, humidity)
                print('temperature: ', temperature, 'and humidity: ', humidity)

            time.sleep(30)

        except RuntimeError as error:
            print(error.args[0])

except KeyboardInterrupt:
    pass

GPIO.cleanup()

if connection.is_open:
    connection.close()
