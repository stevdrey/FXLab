/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxlab.win32;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.ByteByReference;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.win32.W32APIOptions;

/**
 *
 * @author srey
 */
public interface Kernel32 extends com.sun.jna.platform.win32.Kernel32 {
    Kernel32 INSTANCE_API= (Kernel32) Native.loadLibrary("Kernel32", Kernel32.class, W32APIOptions.DEFAULT_OPTIONS);
    
    final int MEM_RELEASE= 0x8000;
    
    /**
     * Reserves, commits, or changes the state of a region of pages in the virtual address space of the calling process. 
     * Memory allocated by this function is automatically initialized to zero.
     * 
     * To allocate memory in the address space of another process, use the VirtualAllocEx function.
     * 
     * @param lpAddress
     *          The starting address of the region to allocate. If the memory is being reserved, the specified address is rounded down to the nearest multiple of the allocation granularity. 
     *          If the memory is already reserved and is being committed, the address is rounded down to the next page boundary. To determine the size of a page and the allocation granularity 
     *          on the host computer, use the GetSystemInfo function. If this parameter is NULL, the system determines where to allocate the region.
     * 
     *          If this address is within an enclave that you have not initialized by calling InitializeEnclave, VirtualAlloc allocates a page of zeros for the enclave at that address. 
     *          The page must be previously uncommitted, and will not be measured with the EEXTEND instruction of the Intel Software Guard Extensions programming model.
     * 
     *          If the address in within an enclave that you initialized, then the allocation operation fails with the ERROR_INVALID_ADDRESS error.
     * 
     * @param dwSize
     *          The size of the region, in bytes. If the lpAddress parameter is NULL, this value is rounded up to the next page boundary. Otherwise, 
     *          the allocated pages include all pages containing one or more bytes in the range from lpAddress to lpAddress+dwSize. 
     *          This means that a 2-byte range straddling a page boundary causes both pages to be included in the allocated region.
     * 
     * @param flAllocationType
     *          The type of memory allocation.
     * 
     * @param flProtect
     *          The memory protection for the region of pages to be allocated. If the pages are being committed, you can specify any one of the memory protection constants.
     *          If lpAddress specifies an address within an enclave, flProtect cannot be any of the following values:
     *          <ul>
     *              <li>PAGE_NOACCESS</li>
     *              <li>PAGE_GUARD</li>
     *              <li>PAGE_NOCACHE</li>
     *              <li>PAGE_WRITECOMBINE</li>
     *          </ul>
     * 
     * @return 
     *      If the function succeeds, the return value is the base address of the allocated region of pages.
     *      If the function fails, the return value is NULL. To get extended error information, call GetLastError.
     */
    LPVOID VirtualAlloc(LPVOID lpAddress, SIZE_T dwSize, DWORD  flAllocationType, DWORD  flProtect);
    
    /**
     * Reads data from an area of memory in a specified process. The entire area to be read must be accessible or the operation fails.
     * 
     * @param hProcess
     *          A handle to the process with memory that is being read. The handle must have PROCESS_VM_READ access to the process.
     * 
     * @param lpBaseAddress
     *          A pointer to the base address in the specified process from which to read. Before any data transfer occurs, 
     *          the system verifies that all data in the base address and memory of the specified size is accessible for read access, 
     *          and if it is not accessible the function fails.
     * 
     * @param lpBuffer
     *          A pointer to a buffer that receives the contents from the address space of the specified process.
     * 
     * @param nSize
     *          The number of bytes to be read from the specified process.
     * 
     * @param lpNumberOfBytesRead
     *          A pointer to a variable that receives the number of bytes transferred into the specified buffer. 
     *          If lpNumberOfBytesRead is NULL, the parameter is ignored.
     * 
     * @return 
     *      If the function succeeds, the return value is nonzero.
     *      If the function fails, the return value is 0 (zero). To get extended error information, call GetLastError.
     *      The function fails if the requested read operation crosses into an area of the process that is inaccessible.
     */
    boolean ReadProcessMemory(HANDLE hProcess, HANDLE lpBaseAddress, char[] lpBuffer, int nSize, HANDLEByReference lpNumberOfBytesRead);
    
    /**
     * Reserves, commits, or changes the state of a region of memory within the virtual address space of a specified process. The function initializes the memory it allocates to zero.
     * To specify the NUMA node for the physical memory, see VirtualAllocExNuma.
     * 
     * @param hProcess
     *          The handle to a process. The function allocates memory within the virtual address space of this process.
     *          The handle must have the PROCESS_VM_OPERATION access right. For more information, see Process Security and Access Rights.
     * 
     * @param lpAddress
     *          The pointer that specifies a desired starting address for the region of pages that you want to allocate.
     *          If you are reserving memory, the function rounds this address down to the nearest multiple of the allocation granularity.
     * 
     *          If you are committing memory that is already reserved, the function rounds this address down to the nearest page boundary. 
     *          To determine the size of a page and the allocation granularity on the host computer, use the GetSystemInfo function.
     * 
     *          If lpAddress is NULL, the function determines where to allocate the region.
     * 
     *          If this address is within an enclave that you have not initialized by calling InitializeEnclave, VirtualAllocEx allocates a page of zeros for the enclave at that address. 
     *          The page must be previously uncommitted, and will not be measured with the EEXTEND instruction of the Intel Software Guard Extensions programming model.
     * 
     *          If the address in within an enclave that you initialized, then the allocation operation fails with the ERROR_INVALID_ADDRESS error.
     * 
     * @param dwSize
     *          The size of the region of memory to allocate, in bytes.
     * 
     *          If lpAddress is NULL, the function rounds dwSize up to the next page boundary.
     * 
     *          If lpAddress is not NULL, the function allocates all pages that contain one or more bytes in the range from lpAddress to lpAddress+dwSize. 
     *          This means, for example, that a 2-byte range that straddles a page boundary causes the function to allocate both pages.
     * 
     * @param flAllocationType
     *          The type of memory allocation.
     * 
     * @param flProtect
     *          The memory protection for the region of pages to be allocated. If the pages are being committed, you can specify any one of the memory protection constants.
     *          If lpAddress specifies an address within an enclave, flProtect cannot be any of the following values:
     *          <ul>
     *              <li>PAGE_NOACCESS</li>
     *              <li>PAGE_GUARD</li>
     *              <li>PAGE_NOCACHE</li>
     *              <li>PAGE_WRITECOMBINE</li>
     *          </ul>
     * 
     * @return 
     *      If the function succeeds, the return value is the base address of the allocated region of pages.
     *      If the function fails, the return value is NULL. To get extended error information, call GetLastError.
     */
    HANDLE VirtualAllocEx(HANDLE hProcess, int lpAddress, int dwSize, int  flAllocationType, int  flProtect);
    
    /**
     * Releases, decommits, or releases and decommits a region of memory within the virtual address space of a specified process.
     * 
     * @param hProcess
     *          A handle to a process. The function frees memory within the virtual address space of the process.
     *          The handle must have the PROCESS_VM_OPERATION access right. For more information, see Process Security and Access Rights.
     * 
     * @param lpAddress
     *          A pointer to the starting address of the region of memory to be freed.
     *          If the dwFreeType parameter is MEM_RELEASE, lpAddress must be the base address returned by the VirtualAllocEx function when the region is reserved.
     * 
     * @param dwSize
     *          The size of the region of memory to free, in bytes.
     *          If the dwFreeType parameter is MEM_RELEASE, dwSize must be 0 (zero). 
     *          The function frees the entire region that is reserved in the initial allocation call to VirtualAllocEx.
     *          If dwFreeType is MEM_DECOMMIT, the function decommits all memory pages that contain one or more bytes in the range from the lpAddress parameter to (lpAddress+dwSize). 
     *          This means, for example, that a 2-byte region of memory that straddles a page boundary causes both pages to be decommitted. 
     *          If lpAddress is the base address returned by VirtualAllocEx and dwSize is 0 (zero), the function decommits the entire region that is allocated by VirtualAllocEx. 
     *          After that, the entire region is in the reserved state.
     * 
     * @param dwFreeType
     *          The type of free operation. 
     * 
     * @return 
     *      If the function succeeds, the return value is a nonzero value.
     *      If the function fails, the return value is 0 (zero). To get extended error information, call GetLastError.
     */
    boolean VirtualFreeEx(HANDLE hProcess, HANDLE lpAddress, int dwSize, int  dwFreeType);
    
    /**
     * Moves a block of memory from one location to another.
     * 
     * @param Destination
     *          A pointer to the starting address of the move destination.
     * 
     * @param Source
     *          A pointer to the starting address of the block of memory to be moved.
     * 
     * @param Length 
     *          The size of the block of memory to move, in bytes.
     */
    void RtlMoveMemory (Pointer Destination, Pointer Source, int Length);
}