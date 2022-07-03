
import os
from flask import Flask, render_template, request, redirect, Response, url_for, make_response
# Imports
import numpy as np
import pandas as pd

# Visualization
import matplotlib.pyplot as plt

# Tensorflow
import tensorflow
from tensorflow import keras
from keras.models import load_model
from keras.preprocessing.image import img_to_array, load_img
from keras.applications.densenet import preprocess_input
from werkzeug.utils import secure_filename

# --------------------------------------------------------------------------------------------------------------------------
app = Flask(__name__)

# Configure variables for Transfer learning
image_size = 224
target_size = (image_size, image_size)
input_shape = (image_size, image_size, 3)
grid_shape = (1, image_size, image_size, 3)

batch_size = 32

# Initialize data to lists.
data = [{'PlantName': 'Apple', 'LeafDisease':'Apple scab'},
        {'PlantName': 'Apple', 'LeafDisease':'Black rot'},
        {'PlantName': 'Apple', 'LeafDisease':'Cedar apple rust'},
        {'PlantName': 'Apple', 'LeafDisease':'Healty'},
        {'PlantName': 'Blueberry', 'LeafDisease':'Healty'},
        {'PlantName': 'Cherry_(including_sour)', 'LeafDisease':'Powdery mildew'},
        {'PlantName': 'Cherry_(including_sour)', 'LeafDisease':'healthy'},
        {'PlantName': 'Corn_(maize)', 'LeafDisease':'Cercospora leaf spot Gray leaf spot'},
        {'PlantName': 'Corn_(maize)', 'LeafDisease':'Common rust'},
        {'PlantName': 'Corn_(maize)', 'LeafDisease':'Northern Leaf Blight'},
        {'PlantName': 'Corn_(maize)', 'LeafDisease':'healthy'},
        {'PlantName': 'Grape', 'LeafDisease':'Black rot'},
        {'PlantName': 'Grape', 'LeafDisease':'Esca_(Black Measles)'},
        {'PlantName': 'Grape', 'LeafDisease':'Leaf blight(Isariopsis Leaf Spot)'},
        {'PlantName': 'Grape', 'LeafDisease':'healthy'},
        
        {'PlantName': 'Orange', 'LeafDisease':'Haunglongbing(Citrus greening)'},
        {'PlantName': 'Peach', 'LeafDisease':'Bacterial_spot'},
        {'PlantName': 'Peach', 'LeafDisease':'healthy'},
        {'PlantName': 'Pepper, bell', 'LeafDisease':'Bacterial_spot'},
        {'PlantName': 'Pepper, bell', 'LeafDisease':'healthy'},
        {'PlantName': 'Potato', 'LeafDisease':'Early blight'},
        {'PlantName': 'Potato', 'LeafDisease':'Late blight'},
        {'PlantName': 'Potato', 'LeafDisease':'healthy'},
        {'PlantName': 'Raspberry', 'LeafDisease':'healthy'},
        {'PlantName': 'Soybean', 'LeafDisease':'healthy'},
        {'PlantName': 'Squash', 'LeafDisease':'Powdery mildew'},
        {'PlantName': 'Strawberry', 'LeafDisease':'Leaf scorch'},
        {'PlantName': 'Strawberry', 'LeafDisease':'healthy'},
        {'PlantName': 'Tomato', 'LeafDisease':'Bacterial spot'},
        {'PlantName': 'Tomato', 'LeafDisease':'Early blight'},
        {'PlantName': 'Tomato', 'LeafDisease':'Late blight'},
        {'PlantName': 'Tomato', 'LeafDisease':'Leaf Mold'},
        {'PlantName': 'Tomato', 'LeafDisease':'Septoria leaf spot'},
        {'PlantName': 'Tomato', 'LeafDisease':'Spider mites, Two-spotted spider mite'},
        {'PlantName': 'Tomato', 'LeafDisease':'Target Spot'},
        {'PlantName': 'Tomato', 'LeafDisease':'Tomato Yellow Leaf Curl Virus'},
        {'PlantName': 'Tomato', 'LeafDisease':'Tomato mosaic virus'},
        {'PlantName': 'Tomato', 'LeafDisease':'healthy'},
        
        ]
 
df = pd.DataFrame(data)
 
def predection(path):
  img = load_img(path, target_size= input_shape)
  i = img_to_array(img)
  im = preprocess_input(i)
  im = np.expand_dims(i, axis=0)
  pred = np.argmax(model.predict(im))

  return pred

  
# load the leaf detection model from disk
model = load_model("./leaf_detector.model")


UPLOAD_FOLDER = './Static/photos'
ALLOWED_EXTENSIONS = set([ 'png', 'jpg', 'jpeg', 'gif'])
app.config['UPLOAD_FOLDER'] = UPLOAD_FOLDER

@app.route('/')
@app.route('/home')
def home():
  id = ''
  row = ''
  errorMessage = ''
  id = request.args.get('id')
  print(id)
  

  errorMessage = request.args.get('errorMessage')

  if id != '' and id != None:
    row = data[int(id)]
  return render_template('home.html',row = row , errorMessage = errorMessage)

def allowed_file(filename):
    return '.' in filename and \
           filename.rsplit('.', 1)[1].lower() in ALLOWED_EXTENSIONS

@app.route('/getPhoto')
@app.route('/getPhoto', methods=["POST"])
def getPhoto():
  file = ''
  filepath=''
  id=''
  message=''
  file = request.files['file']
  print(file)
  if file and allowed_file(file.filename):
    print("iffloop true")
    filename = secure_filename(file.filename)
    filepath = os.path.join(app.config['UPLOAD_FOLDER'], filename)
    print(filepath)
    file.save(filepath)
    id = predection(filepath)
  else: 
    message = "File type not allowed. Please select an image and try again"
  
  print(id)
  return redirect(url_for('home', id = id, errorMessage = message))


if __name__ == '__main__':
    app.run(host='127.0.0.1', port=8080, debug=True)
