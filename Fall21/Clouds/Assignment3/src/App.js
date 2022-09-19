import './App.css';
import React from "react";
import { useState , useEffect } from 'react';
import {makeStyles} from '@material-ui/core/styles';
import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableContainer from '@material-ui/core/TableContainer';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import Paper from '@material-ui/core/Paper';
import DeleteIcon from '@material-ui/icons/Delete';
import AirplanemodeActiveIcon from '@material-ui/icons/AirplanemodeActive';
import FlightTakeoffIcon from '@material-ui/icons/FlightTakeoff';


import {
    Box,
    Button,
    Dialog, DialogActions,
    DialogContentText,
    DialogTitle,
    Divider,
    Grid,
    TextField,
    Typography
} from "@material-ui/core";

const useStyles = makeStyles({
    table: {
        minWidth: 650,
    },
    paper: {
        width: "100%",
        height: "100vh",
        margin: "auto"
    },
    dialog: {
        padding: "5px",
    },
    divider: {
        color: "green",
    },
    title: {
        color: "black",
        backgroundColor: "#9c27b0"
    },
    TableCell: {
        backgroundColor: "#f8bbd0",
        variant: "outlined"
    }

});


const headers = {
    "Content-Type": "application/json",
    "accept": "application/json",
};

const Api = "https://9tgy19yq3l.execute-api.us-east-1.amazonaws.com/flights";
const dropFlight = {
    id: "",
    from: "",
    to: "",
    departure: "",
    arrival: ""}

function App() {
    const classes = useStyles();
    const [open, setOpen] = React.useState(false);
    const [flights, setFlights] = React.useState([]);
    const [newFlight, setNewFlight] = React.useState({
        id: "",
        from: "",
        to: "",
        departure: "",
        arrival: ""
    });
    const [editedFlight, setEditedFlight] = React.useState({...dropFlight});

    // Fetch all Flights
    useEffect(() => {
        console.log(Api);
    
        fetch(`${Api}`)
          .then((response) => response.json())
          .then((data) => {
            setFlights(data.Items);
          });
      }, []);
    

    const addFlight = async () => {
        const requestOptions = {
            method: "PUT",
            headers: headers,
            body: JSON.stringify({
                ...newFlight,
                id: newFlight.id,
                from: newFlight.from,
                to: newFlight.to,
                departure: newFlight.departure,
                arrival: newFlight.arrival
                
            })
        };
        try {
            await (await fetch(Api, requestOptions)).json();
            setFlights([...flights, {
                ...newFlight,
                id: newFlight.id,
                from: newFlight.from,
                to: newFlight.to,
                departure: newFlight.departure,
                arrival: newFlight.arrival
            }]);
        } catch (err) {
            alert("THERE WAS AN ERROR SAVING THE NEW FLIGHT: " + err.message);
        }
    }

    const editFlight =async () => {
        const requestOptions = {
            method: "PUT",
            headers: headers,
            body: JSON.stringify({
                ...editedFlight,
                from: editedFlight.from,
                to: editedFlight.to,
                departure: editedFlight.departure,
                arrival: editedFlight.arrival
                
            })
        };
        try {
            await (await fetch(Api, requestOptions)).json();
            const index = flights.findIndex(f => f.id === editedFlight.id);
            const flightsNew = [...flights];
            flightsNew[index] = {...editedFlight, 
                from: editedFlight.from,
                to: editedFlight.to,
                departure: editedFlight.departure,
                arrival: editedFlight.arrival};
            setFlights(flightsNew);
        } catch(err) {
            alert("THERE WAS AN ERROR UPDATING THE Flight: " + err.message);
        }
    }

    const getPopup = () => {
        if (!open) return null;
        const close = () => {
            setEditedFlight({...dropFlight});
            setOpen(false);
        }
        const update = () => {
            editFlight().then(_ => close());
        }
        return (
            <Dialog className={classes.dialog} fullWidth={true} open={open} aria-labelledby="company-dialog-popup">
                <DialogTitle className={classes.title}>
                    {"New Flight"}
                </DialogTitle>
                <Divider classes={{
                    root: classes.divider
                }}/>
                <Box mr={2} ml={2} mt={0}>
                    <DialogContentText style={{marginTop: "20px"}}>
                        {"Update the flight details below"}
                    </DialogContentText>
                    <Grid container spacing={3} direction='column'>
                        <Grid item>
                            <TextField
                                onChange={(event) =>
                                    setEditedFlight({...editedFlight, id: event.target.value})}
                                autoFocus
                                id="id"
                                label={"Flight Number"}
                                type="text"
                                fullWidth
                                value={editedFlight.id}
                            />
                        </Grid>
                        <Grid item>
                        <TextField
                                onChange={(event) =>
                                    setEditedFlight({...editedFlight, from: event.target.value})}
                                id="from"
                                label={"From"}
                                type="text"
                                fullWidth
                                value={editedFlight.from}
                            />
                        </Grid>
                        <Grid item>
                            <TextField
                                onChange={(event) =>
                                    setEditedFlight({...editedFlight, to: event.target.value})}
                                id="to"
                                label={"To"}
                                type="text"
                                fullWidth
                                value={editedFlight.to}
                            />
                        </Grid>
                        <Grid item>
                            <TextField 
                                   onChange={(e) =>
                                       setEditedFlight({...editedFlight, departure: e.target.value})}
                                   id="departure"
                                   label={"Departure"}
                                   value={editedFlight.departure}
                                   type="text" 
                                   fullWidth
                                       
                            />
                        </Grid>
                        <Grid item>
                            <TextField 
                                   onChange={(e) =>
                                       setEditedFlight({...editedFlight, arrival: e.target.value})}
                                   id="arrival"
                                   label={"Arrival"}
                                   value={editedFlight.arrival}
                                   type="text" 
                                   fullWidth
                                       
                            />
                        </Grid>
                    </Grid>
                </Box>
                <DialogActions style={{marginTop: "20px"}}>
                    <Button
                        variant={"filled"}
                        onClick={close}
                        color="secondary"
                    >
                        CANCEL
                    </Button>
                    <Button
                        variant={"filled"}
                        onClick={update}
                        color="primary">
                        UPDATE
                    </Button>
                </DialogActions>
            </Dialog>
        )
    }

    const deleteFlight = async (id) => {
        const requestOptions = {
            method: "DELETE",
            headers: headers,
        };
        try {
            await (await fetch(`${Api}\\${id}`, requestOptions));
            setFlights(flights.filter(f => f.id !== id));
        } catch (err) {
            alert("THERE WAS AN ERROR DELETING THE FLIGHT: " + err.message);
        }
    }

    return (
        <React.Fragment>
            <Grid direction={"column"} className={classes.paper} container justifyContent="center"
                  alignItems={"center"}>
                <Grid item><Typography align={"center"} gutterBottom variant="h2">Flights Table</Typography></Grid>
                <Grid item>
                    <Paper>
                        <TableContainer style={{maxHeight: "70vh"}} component={Paper}>
                            <Table stickyHeader aria-label="simple table">
                                <TableHead >
                                    <TableRow backgroundColor="#9c27b0" >
                                        <TableCell align="center" className={classes.TableCell}>Flight Number</TableCell>
                                        <TableCell align="center" className={classes.TableCell}  >From</TableCell>
                                        <TableCell align="center" className={classes.TableCell}  >To</TableCell>
                                        <TableCell align="center" className={classes.TableCell} >Departure</TableCell>
                                        <TableCell align="center" className={classes.TableCell} >Arrival</TableCell>
                                        <TableCell align="center" className={classes.TableCell} >Actions</TableCell>
                                    </TableRow>
                                </TableHead>
                                <TableBody>
                                    {flights.map(c => (
                                        <TableRow key={c.id}>
                                            <TableCell align={"center"}>{c.id}</TableCell>
                                            <TableCell align={"center"}>{c.from}</TableCell>
                                            <TableCell align={"center"}>{c.to}</TableCell>
                                            <TableCell align={"center"}>{c.departure}</TableCell>
                                            <TableCell align={"center"}>{c.arrival}</TableCell>
                                            <TableCell align={"center"}>
                                                <Button variant="outlined" color="primary" startIcon={<AirplanemodeActiveIcon />} onClick={() => {
                                                    setEditedFlight({...c});
                                                    setOpen(true);
                                                }}>
                                                    Update
                                                </Button>
                                                <Button variant="outlined" color="secondary" startIcon={<DeleteIcon />} onClick={() => deleteFlight(c.id)}>
                                                    Delete
                                                </Button>
                                            </TableCell>
                                        </TableRow>
                                    ))}
                                </TableBody>
                            </Table>
                        </TableContainer>
                        <TableRow>
                            <TableCell component="th" scope={"row"}>
                                <TextField id="new_flight_number" label="New Flight Number" value={newFlight.id}
                                           onChange={(e) =>
                                               setNewFlight({...newFlight, id: e.target.value})}
                                />
                            </TableCell>
                            <TableCell align={"center"}>
                                <TextField id="new_From" label="From" value={newFlight.from}
                                           onChange={(e) =>
                                               setNewFlight({...newFlight, from: e.target.value})}
                                />
                            </TableCell>
                            <TableCell align={"center"}>
                                <TextField id="new_To" label="To" value={newFlight.to}
                                            onChange={(e) =>
                                                setNewFlight({...newFlight, to: e.target.value})}
                                    />
                            </TableCell>
                            <TableCell align={"center"}>
                                <TextField id="new_departure" label="Departure" value={newFlight.departure}
                                           onChange={(e) =>
                                               setNewFlight({...newFlight, departure: e.target.value})}
                                />
                            </TableCell>
                            <TableCell align={"center"}>
                                <TextField id="new_arrival" label="Arrival" value={newFlight.arrival}
                                           onChange={(e) =>
                                               setNewFlight({...newFlight, arrival: e.target.value})}
                                />
                            </TableCell>
                            <TableCell align={"center"}>
                                <Button variant="contained" color='#f44336' startIcon={<FlightTakeoffIcon />} onClick={addFlight}>Add New Flight</Button>
                            </TableCell>
                        </TableRow>
                    </Paper>
                </Grid>
            </Grid>
            {getPopup()}
        </React.Fragment>
    );
}

export default App;
