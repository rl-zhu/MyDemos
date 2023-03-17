import Head from 'next/head'
import Image from 'next/image'
import { Inter } from 'next/font/google'
import styles from '@/styles/Home.module.css'
import { useEffect, useState } from 'react'
import React from 'react';
import axios from 'axios';
import { Alert, Tooltip } from 'antd';
// import styles from "./styles.module.css"
// import Logo from "../../static/logo.png"




const inter = Inter({ subsets: ['latin'] })

export default function Home() {

  const [urlFormatAlert, showUrlFormatAlert] = useState(false);
  const [imgUrls, setImgUrls] = useState([]);
  const [inputValue, setInputValue] = useState('');
  const [reqSendingAlert, showreqSendingAlert] = useState(false);
  const [reqSendingSuccessAlert, showReqSendingSuccessAlert] = useState(false);
  const [imgNums, setImgNums] = useState("")

  const getImgs = async () => {
    setImgUrls([]);
    showReqSendingSuccessAlert(false)

    const pattern = /^(https?:\/\/)([\da-z.-]+)\.([a-z.]{2,6})([\/\w .-]*)*\/?$/;

    if (!pattern.test(inputValue)) {


      showUrlFormatAlert(true)
    } else {
      try {
        const requestBody = {
          requrl: inputValue
        };
        showreqSendingAlert(true)
        const response = await axios.post('http://localhost:8088/images', requestBody);

        showUrlFormatAlert(false)

        const urls = response.data.map(imageData => {
          const base64String = imageData.body; // the base64-encoded string
          const byteCharacters = atob(base64String); // convert base64 to binary
          const byteNumbers = new Array(byteCharacters.length);
          for (let i = 0; i < byteCharacters.length; i++) {
            byteNumbers[i] = byteCharacters.charCodeAt(i);
          }
          const byteArray = new Uint8Array(byteNumbers); // create Uint8Array from binary data
          const blob = new Blob([byteArray], { type: 'image/png' }); // create Blob from Uint8Array

          const contentType = imageData.headers['Content-Type'];

          const blobUrl = URL.createObjectURL(blob, { type: contentType });
          return blobUrl;
        });

        showreqSendingAlert(false)

        setImgUrls(urls);
        setImgNums("Get " + urls.length + " images intotal")
        showReqSendingSuccessAlert(true)

      } catch (error) {
        showUrlFormatAlert(true)
        showreqSendingAlert(false)
      }
    }
  };


  const getAllImgs = async () => {
    setImgUrls([]);
    showReqSendingSuccessAlert(false)

    const pattern = /^(https?:\/\/)([\da-z.-]+)\.([a-z.]{2,6})([\/\w .-]*)*\/?$/;

    if (!pattern.test(inputValue)) {


      showUrlFormatAlert(true)
    } else {

      try {
        const requestBody = {
          requrl: inputValue
        };

        showreqSendingAlert(true)
        const response = await axios.post('http://localhost:8088/images/all', requestBody);

        showUrlFormatAlert(false)

        const urls = response.data.map(imageData => {
          const base64String = imageData.body; // the base64-encoded string
          const byteCharacters = atob(base64String); // convert base64 to binary
          const byteNumbers = new Array(byteCharacters.length);
          for (let i = 0; i < byteCharacters.length; i++) {
            byteNumbers[i] = byteCharacters.charCodeAt(i);
          }
          const byteArray = new Uint8Array(byteNumbers); // create Uint8Array from binary data
          const blob = new Blob([byteArray], { type: 'image/png' }); // create Blob from Uint8Array

          const contentType = imageData.headers['Content-Type'];

          const blobUrl = URL.createObjectURL(blob, { type: contentType });
          return blobUrl;
        });
        showreqSendingAlert(false)

        setImgUrls(urls);
        setImgNums("Get " + urls.length + " images in total!")
        showReqSendingSuccessAlert(true)
      } catch (error) {
        showUrlFormatAlert(true)
        showreqSendingAlert(false)
      }
    }
  };


  const handleInputChange = (event) => {

    showUrlFormatAlert(false)
    setInputValue(event.target.value);
  };


  const clearDB = async () => {
    setImgUrls([]);
    showReqSendingSuccessAlert(false)
    const response = await axios.get('http://localhost:8088/cleardb');
    console.dir(response)
  }


  return (
    <div className={`bg-slate-50 min-h-[100vh] px-[3rem] pt-[3rem] ${styles.bg}`}>


      <div className="flex flex-col">

        <div className={` ${styles.center}`}>


          <div className={`text-[3rem] ${styles.legend} my-[3rem]`}>Image Finder by Ruilin Zhu</div>

        </div>
        <div className={` ${styles.center}`}>
          <label>
            <span className="text-[1.5rem] mr-[1rem]">Search for webpage:</span>

            <input className="border-2 border-slate-300 rounded-lg px-[1rem] py-[0.5rem] text-[1.4rem] w-[44rem]" type="text" value={inputValue} onChange={handleInputChange}
              placeholder="Please input URLs starting with http:// or https://"
            />
          </label>
          {reqSendingSuccessAlert && <Alert message={imgNums} type="success" closable style={{ width: "40rem", fontSize: "1.3rem", marginTop: "1rem", fontWeight: "600" }}>nihao</Alert>}
          {reqSendingAlert && <Alert message="Sending to the backend! Please wait..." type="warning" closable style={{ width: "40rem", fontSize: "1.3rem", marginTop: "1rem", fontWeight: "600" }} />}
          {urlFormatAlert && <div className="text-rose-500 italic text-[1.2rem] mb-[1rem]">Please check the url format and make sure it starts with &quot;http://&quot; or &quot;https://&quot;; Or change another url</div>}
          <div className="mt-[2rem]">
            <Tooltip placement="bottomLeft" title="Get images from current page but not its children pages"
              overlayInnerStyle={{ fontSize: "1.2rem", width: "30rem" }}
            >
              <button className="border-2 border-slate-300 rounded-lg px-[1rem] py-[0.5rem] text-[1.4rem] mr-[2rem] bg-slate-50" type="submit" onClick={getImgs}>Get current page images</button>

            </Tooltip>
            <Tooltip placement="bottomLeft" title="Search current page add iteratelly search its children pages, and return all images"

              overlayInnerStyle={{ fontSize: "1.2rem", width: "30rem" }}
            >
              <button className="border-2 border-slate-300 rounded-lg px-[1rem] py-[0.5rem] text-[1.4rem] mr-[2rem] bg-slate-50" type="submit" onClick={getAllImgs}>Get images from current page and its all subpages</button>
            </Tooltip>
            <Tooltip placement="bottomRight" title="Clear frontend and backend data"
              overlayInnerStyle={{ fontSize: "1.2rem", width: "30rem" }}
            >
              <button className="border-2 border-slate-300 rounded-lg px-[1rem] py-[0.5rem] text-[1.4rem] bg-slate-50" onClick={clearDB}>Clear</button>
            </Tooltip>
          </div>
        </div>
      </div>

      {imgUrls.length !== 0 &&
        <div className="flex justify-center mt-[2rem]">
          <div className='grid grid-cols-[1fr_1fr_1fr] gap-[1rem] bg-slate-200 p-[2rem] border-2 border-slate-300 rounded-lg min-w-[40rem] max-w-[120rem] w-[75%] mt-[2rem]'>
            {imgUrls && imgUrls.map((imageUrl, index) =>
              <div key={index} className="bg-[#7FA8BAA3] rounded-lg">

                <img className="w-[100%]" key={index} src={imageUrl} alt={`Image ${index}`} onError={(e) => e.target.style.display = "none"} />

              </div>)}
          </div>
        </div>}
    </div>
  )
}
