import Link from 'next/link'
import React from 'react'
import { Button } from 'antd';

const Index = () => {
  return (
    <div>
      <div className="my-[2rem] font-[800] text-[2.5rem]">Welcome to Ruilin&#39;s Spring Boot projects</div>
      Click the following buttons to router different page

      <div>
        <ul>
          <li>
            <Link href="image_finder"><Button>Image Finders</Button></Link>
          </li>
          <li>
            <Link href="multithread"><Button>MultiThread to parse the csv files</Button></Link>
          </li>
        </ul>


      </div>
    </div>
  )
}

export default Index