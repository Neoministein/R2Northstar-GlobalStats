import { Column } from 'primereact/column';
import BackendService from '../../../demo/service/BackendService';
import TopResultTable from '../../../demo/components/TopResultTable';

const WinsPage = () => {

    return (
        <TopResultTable 
            title="Top Player Kills" 
            getGlobalRanking={() => BackendService.getTopWins()}
            columns={
                [<Column header="Player Name" field="playerName"/>,
                <Column header="Wins" field="win" />]}/> 
    );
};

export default WinsPage;