import Head from 'next/head'
import Image from 'next/image'
// import styles from '../../styles/Home.module.css'
import ProjectHeaderComp from '@/components/header-projects'
import { useEffect, useState } from 'react'
import React from 'react';
import axios from 'axios';
import { Alert, Divider, Tooltip } from 'antd';
import styles from './styles.module.scss'



export default function Home() {

  const [urlFormatAlert, showUrlFormatAlert] = useState(false);
  const [imgUrls, setImgUrls] = useState([]);
  const [inputValue, setInputValue] = useState('');
  const [reqSendingAlert, showreqSendingAlert] = useState(false);
  const [reqSendingSuccessAlert, showReqSendingSuccessAlert] = useState(false);
  const [imgNums, setImgNums] = useState("")
  const [timeInSearch, setTimeInSearch] = useState(0)

  const getImgs = async () => {
    setImgUrls([]);
    showreqSendingAlert(false)
    showUrlFormatAlert(false)
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
        const response = await axios.post('http://localhost:8088/project/images', requestBody);

        showUrlFormatAlert(false)

        handleImageData(response.data)

      } catch (error) {
        showUrlFormatAlert(true)
        showreqSendingAlert(false)
        console.dir(error)
      }
    }
  };




  const getAllImgs = async () => {
    setImgUrls([]);
    showreqSendingAlert(false)
    showUrlFormatAlert(false)
    showReqSendingSuccessAlert(false)

    const pattern = /^(https?:\/\/)([\da-z.-]+)\.([a-z.]{2,6})([\/\w .-]*)*\/?$/;

    if (!pattern.test(inputValue)) {

    } else {
      try {
        const requestBody = {
          requrl: inputValue
        };
        showreqSendingAlert(true)
        const response = await axios.post('http://localhost:8088/project/images/all', requestBody);
        showUrlFormatAlert(false)
        handleImageData(response.data)

      } catch (error) {
        showUrlFormatAlert(true)
        showreqSendingAlert(false)
      }
    }
  };


  const getImgsMulti = async () => {
    setImgUrls([]);
    showreqSendingAlert(false)
    showUrlFormatAlert(false)
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
        const response = await axios.post('http://localhost:8088/project/multi/images', requestBody);

        showUrlFormatAlert(false)

        handleImageData(response.data)

      } catch (error) {
        showUrlFormatAlert(true)
        showreqSendingAlert(false)
      }
    }
  };




  const getAllImgsMulti = async () => {
    setImgUrls([]);
    showreqSendingAlert(false)
    showUrlFormatAlert(false)
    showReqSendingSuccessAlert(false)

    const pattern = /^(https?:\/\/)([\da-z.-]+)\.([a-z.]{2,6})([\/\w .-]*)*\/?$/;

    if (!pattern.test(inputValue)) {

    } else {
      try {
        const requestBody = {
          requrl: inputValue
        };
        showreqSendingAlert(true)
        const response = await axios.post('http://localhost:8088/project/multi/images/all', requestBody);
        showUrlFormatAlert(false)
        handleImageData(response.data)

      } catch (error) {
        showUrlFormatAlert(true)
        showreqSendingAlert(false)
      }
    }
  };


  const handleImageData = (datas) => {
    setTimeInSearch(datas.totalTime)


    const urls = datas.data.map(imageData => {
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
    setImgNums("Get " + urls.length + " images and spend " + datas.totalTime + " milliseconds in fetching image process")
    showReqSendingSuccessAlert(true)

  }

  const handleInputChange = (event) => {

    showUrlFormatAlert(false)
    setInputValue(event.target.value);
  };

  const handleInputClick = (event) => {
    if (event.target.value == "http://www.r-zhu.com") {
      setInputValue("");
    }
  }

  const handleInputDefault = (event) => {
      setInputValue("http://www.r-zhu.com");
  }

  const clearDB = async () => {
    setImgUrls([]);
    showReqSendingSuccessAlert(false)
    const response = await axios.get('http://localhost:8088/project/cleardb');
    setTimeInSearch(0)
  }


  return (
    <>    <ProjectHeaderComp activeSection={"image_finder"} />
      <div className={`bg-slate-50 min-h-[100vh] px-[2rem] pt-[3rem]  ${styles.bg}`}>


        <div className="flex flex-col ">

          <div className={` ${styles.center}`}>


            <div className={`xl:text-[3rem]  text-[4rem]  ${styles.legend} ${styles.header} my-[3rem]`}>Image Finder by Ruilin Zhu</div>

          </div>
          <div className={` ${styles.center}`}>
            <label>
              <span className="xl:text-[1.5rem] text-[2rem] mr-[1rem]">Search for webpage:</span>

              <input className="border-2 border-slate-300 rounded-lg px-[1rem] py-[0.5rem] text-[1.4rem] w-[44rem] italic text-slate-600" type="text" value={inputValue} onChange={handleInputChange}
                onClick={handleInputClick}
                placeholder="Please input URLs starting with http:// or https://"
              />
            </label>
            {reqSendingSuccessAlert && <Alert message={imgNums} type="success" closable style={{ width: "40rem", fontSize: "1rem", marginTop: "1rem", fontWeight: "500" }}></Alert>}
            {reqSendingAlert && <Alert message="Sending to the backend! Please wait..." type="warning" closable style={{ width: "40rem", fontSize: "1rem", marginTop: "1rem", fontWeight: "500" }} />}
            {urlFormatAlert && <div className="text-rose-500 italic text-[1.2rem] mb-[1rem]">Please check the url format and make sure it starts with &quot;http://&quot; or &quot;https://&quot;; Or change another url</div>}

            <div className='grid grid-cols-[1fr_1fr] md:grid-cols-[1fr] gap-4'>

              <div className="mt-[2rem] text-[1.2rem] ">
                <div className='w-[90%] bg-slate-200 rounded-xl px-[1.5rem] pt-[2rem] pb-[4rem] min-w-[30rem]'>

                  <div className='w-[80%] '>
                    <div className='rounded-xl bg-slate-800 w-[13rem] text-slate-200 text-[1.5rem] px-[0.5rem] text-center mb-[1rem]'>Single Thread</div>
                    {/* <Divider orientation="left" style={{ fontSize: "1.2rem", marginTop: "0rem" }}>Single Thread</Divider> */}
                  </div>
                  <Tooltip placement="bottomLeft" title="Get images from current page but not its children pages"
                    overlayInnerStyle={{ fontSize: "1rem", width: "20rem" }}
                  >
                    <button className="border-2 border-slate-300 rounded-lg 
                  px-[0.8rem] py-[0.5rem] text-[1rem] mr-[2rem] bg-slate-50"
                      type="submit" onClick={getImgs}>
                      Get images
                    </button>

                  </Tooltip>
                  <Tooltip placement="bottomLeft" title="Search current page add iteratelly search its children pages, and return all images"

                    overlayInnerStyle={{ fontSize: "1rem", width: "20rem" }}
                  >
                    <button className="border-2 border-slate-300 rounded-lg px-[0.8rem] py-[0.5rem] text-[1rem] mr-[2rem] bg-slate-50" type="submit" onClick={getAllImgs}>Get images from all subpages</button>
                  </Tooltip>

                </div>
              </div>

              <div className="mt-[2rem] text-[1.2rem] ">
                <div className='w-[90%] bg-slate-200 rounded-xl px-[1.5rem] pt-[2rem] pb-[4rem] min-w-[30rem] '>

                  <div className='rounded-xl bg-slate-800 w-[13rem] text-slate-200 text-[1.5rem] px-[0.5rem] text-center mb-[1rem]'>Multi-Thread</div>
                  {/* <div className='w-[80%]'><Divider orientation="left" style={{ fontSize: "1.2rem", marginTop: "0rem" }}>Multi-Thread</Divider></div> */}
                  <Tooltip placement="bottomLeft" title="Get images from current page but not its children pages"
                    overlayInnerStyle={{ fontSize: "1rem", width: "20rem" }}
                  >
                    <button className="border-2 border-slate-300 rounded-lg 
                 px-[0.8rem] py-[0.5rem] text-[1rem] mr-[2rem] bg-slate-50"
                      type="submit"
                      onClick={getImgsMulti}>Get images</button>

                  </Tooltip>
                  <Tooltip placement="bottomLeft" title="Search current page add iteratelly search its children pages, and return all images"

                    overlayInnerStyle={{ fontSize: "1rem", width: "20rem" }}
                  >
                    <button className="border-2 border-slate-300 rounded-lg px-[0.8rem] py-[0.5rem] text-[1.0rem] mr-[2rem] bg-slate-50" type="submit" onClick={getAllImgsMulti}>Get images from all pages</button>
                  </Tooltip>
                </div>
              </div>

            </div>

            <Tooltip placement="bottomRight" title="Clear frontend and backend data"
              overlayInnerStyle={{ fontSize: "1.2rem", width: "30rem" }}
            >
              <button className="border bg-slate-500 text-white font-[800] rounded-lg px-[1rem] py-[0.5rem] text-[1.2rem] mt-[2rem] bg-slate-50" onClick={clearDB}>
                Clear</button>
            </Tooltip>

            <button className="ml-[2rem] border  text-white font-[800] rounded-lg px-[1rem] py-[0.5rem] text-[1.2rem] mt-[2rem] bg-red-400" onClick={handleInputDefault}>
                Try www.r-zhu.com </button>

            
          </div>
        </div>

        {imgUrls.length !== 0 &&
          <div className="flex justify-center mt-[2rem]">
            <div className='grid grid-cols-[1fr_1fr_1fr] gap-[1rem] bg-slate-200 p-[2rem] border-2 border-slate-300 rounded-lg min-w-[40rem] max-w-[120rem] w-[75%] mt-[2rem]'>
              {imgUrls && imgUrls.map((imageUrl, index) =>
                <div key={index} className="bg-[#7FA8BAA3] rounded-lg">
                  {/* <img className="w-[100%]" key={index} src={imageUrl} alt={`Image ${index}`} onError={(e) => e.target.style.display = "none"} /> */}

                  <Image className="w-full h-auto"
                    width="0"
                    height="0"

                    sizes="100vw"
                    key={index} src={imageUrl} alt={`Image ${index}`} onError={(e) => e.target.style.display = "none"}
                  />

                </div>)}
            </div>
          </div>}


      </div>
    </>
  )
}
