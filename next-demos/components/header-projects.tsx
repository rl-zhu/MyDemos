import React, { ReactNode, useEffect, useState } from 'react'
import type { MenuProps } from 'antd';
import { Button, Dropdown, Space } from 'antd';
import styles from './styles.module.scss'

interface HeaderProps {
    //   children: ReactNode;
    activeSection: String;
}

const ProjectHeaderComp: React.FC<HeaderProps> = ({ activeSection }) => {

    const titleArray = ["#image_finder"]

    const handleScrollToHome = (e: any) => {
        const target = e.target.getAttribute("title");
        // const section = document.getElementById(target);
        const section = document.querySelector(target);
        const sectionTop = section.offsetTop;
        const sectionHeight = section.offsetHeight;
        const offset = 3.5 * parseInt(getComputedStyle(document.documentElement).fontSize);
        window.scrollTo({
            top: sectionTop - offset,
            behavior: "smooth"
        });

    }




    return (
        <>

            <div className={`header flex justify-between  shadow-md  top-0 px-[3rem] items-end pb-[1rem] ${styles.header}`} style={{ position: "sticky" }}>
                <div className='left'>
                    <ul>
                        <a title={"back to home"} href={"/"} > <li className='text-[0.9rem] font-[900]'>Home</li></a>
                    </ul>

                </div>
                <div className='right'>
                    <ul className='flex w-[60vw] justify-end  font-[900] text-[1.1rem]'>

                        {titleArray.map((title, index) =>
                            <li className={` underline-offset-8 ${activeSection === title.substring(1) ? 'underline' : 'hover:underline'} decoration-[2.5px] decoration-sky-500/30 capitalize ml-[2rem]`}
                                key={index}>

                                <a title={title} href={"/projects/" + title.substring(1)} >{title.substring(1)}</a>
                            </li>)}


                    </ul>

  

                </div>

            </div>


            {/* {children} */}


        </>

    );
};

export default ProjectHeaderComp;

