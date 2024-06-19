from sqlalchemy import create_engine, update, Table, MetaData
from sqlalchemy.orm import sessionmaker
import pandas as pd

# Create a connection to the database
engine = create_engine('mysql://root:1234@localhost/ubike')

# Initialize metadata object
metadata = MetaData()

# Define the pillars table
stations = Table('stations', metadata, autoload_with=engine)

# Create a session
Session = sessionmaker(bind=engine)
session = Session()

# JSON object
data = pd.read_json('Taoyuan.json')

data['StationID'] = data['StationID'].astype(str)
data['StationUID'] = data['StationUID'].astype(str)

# Iterate over each row in the DataFrame
for _, station in data.iterrows():
    # Find the row with the specific StationID and replace it with StationUID
    stmt = (
        update(stations).
        where(stations.c.stationID == station['StationID']).
        values(stationID = station['StationUID'])
    )
    session.execute(stmt)

session.commit()