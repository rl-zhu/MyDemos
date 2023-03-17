import React from 'react'

import axios from 'axios';
import { useState } from 'react';
import { Button } from 'antd';

const MultiThread = () => {

    const [files, setFiles] = useState([]);
    // const [fileNames, setFileNames] = useState([]);

    const handleFileChange = (event) => {
        setFiles(event.target.files);
    };

    const handleUploadClick = async () => {
        if (!files || files.length === 0) {
            return;
        }

        const formData = new FormData();
        for (let i = 0; i < files.length; i++) {
            formData.append(`files`, files[i]);
        }

        try {
            const response = await axios.post('http://localhost:8088/users', formData);
            alert("Sent to the backend");
        } catch (error) {
            console.error(error);
            alert('An error occurred while uploading the file.');
        }
    };


    const [datas, setDatas] = useState([]);
    const handleGetTheData = async () => {

        try {
            const response = await axios.get('http://localhost:8088/users');
            // console.dir(response)
            setDatas(response.data)
        } catch (error) {
            console.error(error);
            alert('An error occurred while uploading the file.');
        }
        
    };

    const handleClear = () => {
        setDatas([])
        setFiles([])

    }



    return (
        <div className="flex justify-center flex-col items-center bg-gradient-to-r from-cyan-500 to-blue-500 h-[100vh]">
           <div className="font-[700] text-[3rem] text-cyan-900 font-serif">Multithread in springboot</div>
            <div className="mt-[5rem] h-[10rem]">

                <Button style={{backgroundColor:"white"}}><label htmlFor="fileInput"  >Choosing files</label></Button>
                <input id="fileInput" type="file" name="fileupload" onChange={handleFileChange} multiple style={{ display: "none" }}></input>
                <Button onClick={handleUploadClick} className="ml-[2rem]" style={{backgroundColor:"white"}}>Upload</Button>
                

                <Button className='ml-[2rem]' onClick={handleGetTheData} style={{backgroundColor:"white"}}>Get the data size</Button>
                
                {datas.length != 0 && <span className="ml-[1rem]">We have {datas.length} datas in the database</span>}
                <div className="mt-[0.5rem]"><Button onClick={handleClear} style={{backgroundColor:"white"}}>Clear</Button></div>
                {files.length > 0 && (
                    <ul className="text-[0.8rem] ml-[4px] mt-[2rem] font-[700]">
                        {Array.from(files).map((file, index) => (
                            <li key={index}>{file.name}</li>
                        ))}
                    </ul>
                )}

            </div>

           



        </div>
    )
}

export default MultiThread